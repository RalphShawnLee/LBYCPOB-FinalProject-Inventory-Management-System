package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonPurchaseOrderRepositoryTest {
    private final Path productFile = Path.of("target/test-data/products-" + System.nanoTime() + ".json");
    private final Path supplierFile = Path.of("target/test-data/suppliers-" + System.nanoTime() + ".json");
    private final Path orderFile = Path.of("target/test-data/purchase-orders-" + System.nanoTime() + ".json");

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(productFile);
        Files.deleteIfExists(supplierFile);
        Files.deleteIfExists(orderFile);
    }

    @Test
    void reloadedOrderResolvesToTheSameLiveProductInstance() {
        JsonProductRepository productRepository = new JsonProductRepository(productFile.toString());
        productRepository.save(new Product("SKU-1", "Widget", "Tools", 10.0, 5, 3));
        JsonSupplierRepository supplierRepository = new JsonSupplierRepository(supplierFile.toString());
        supplierRepository.save(new Supplier("Acme", "acme@example.com", "123"));

        Product product = productRepository.findAll().get(0);
        Supplier supplier = supplierRepository.findAll().get(0);
        JsonPurchaseOrderRepository firstRun = new JsonPurchaseOrderRepository(orderFile.toString(), productRepository, supplierRepository);
        firstRun.save(new PurchaseOrder(supplier, List.of(new PurchaseOrderLine(product, 10))));
        int firstId = firstRun.findAll().get(0).getId();

        JsonPurchaseOrderRepository reloadedRun = new JsonPurchaseOrderRepository(orderFile.toString(), productRepository, supplierRepository);
        PurchaseOrder reloadedOrder = reloadedRun.findAll().get(0);

        assertEquals(1, reloadedRun.findAll().size());
        assertSame(product, reloadedOrder.getLines().get(0).getProduct());
        assertEquals(10, reloadedOrder.getLines().get(0).getQuantity());

        PurchaseOrder newOrder = new PurchaseOrder(supplier, List.of(new PurchaseOrderLine(product, 1)));
        assertTrue(newOrder.getId() > firstId, "id counter must advance past ids already on disk");
    }
}
