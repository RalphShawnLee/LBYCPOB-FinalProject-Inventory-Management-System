package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonProductRepositoryTest {
    private final Path file = Path.of("target/test-data/products-" + System.nanoTime() + ".json");

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(file);
    }

    @Test
    void savedProductSurvivesReloadFromDisk() {
        JsonProductRepository first = new JsonProductRepository(file.toString());
        first.save(new Product("SKU-1", "Widget", "Tools", 12.5, 7, 3));

        JsonProductRepository reloaded = new JsonProductRepository(file.toString());

        assertEquals(1, reloaded.findAll().size());
        Product product = reloaded.findAll().get(0);
        assertEquals("SKU-1", product.getSku());
        assertEquals("Widget", product.getName());
        assertEquals(12.5, product.getPrice());
        assertEquals(7, product.getQuantity());
        assertEquals(3, product.getReorderThreshold());
    }

    @Test
    void updatedProductSurvivesReloadFromDisk() {
        JsonProductRepository first = new JsonProductRepository(file.toString());
        first.save(new Product("SKU-1", "Widget", "Tools", 12.5, 7, 3));
        Product saved = first.findAll().get(0);
        saved.setQuantity(20);
        first.update(saved);

        JsonProductRepository reloaded = new JsonProductRepository(file.toString());

        assertEquals(20, reloaded.findAll().get(0).getQuantity());
    }
}
