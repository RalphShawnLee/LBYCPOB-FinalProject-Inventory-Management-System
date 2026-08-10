package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.OrderStatus;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryPurchaseOrderRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryStockMovementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseOrderServiceTest {
    @Test
    void receivingOrderIncreasesStockAndLogsInMovement() {
        StockMovementService stockMovementService = new StockMovementService(new InMemoryStockMovementRepository());
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService(new InMemoryPurchaseOrderRepository(), stockMovementService);
        Product product = new Product("SKU-1", "Widget", "Tools", 10.0, 5, 3);
        Supplier supplier = new Supplier("Acme", "acme@example.com", "123");

        PurchaseOrder order = purchaseOrderService.createOrder(supplier, List.of(new PurchaseOrderLine(product, 10)));
        purchaseOrderService.receiveOrder(order);

        assertEquals(15, product.getQuantity());
        assertEquals(OrderStatus.RECEIVED, order.getStatus());
        assertEquals(1, stockMovementService.getAll().size());
        assertEquals(MovementType.IN, stockMovementService.getAll().get(0).getType());
    }
}
