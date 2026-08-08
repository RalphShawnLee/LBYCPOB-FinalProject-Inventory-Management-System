package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(new InMemoryProductRepository());
    }

    @Test
    void lowStockCountCountsProductsAtOrBelowThreshold() {
        productService.addProduct("SKU-1", "Widget", "Tools", 10.0, 2, 5);
        productService.addProduct("SKU-2", "Gadget", "Tools", 20.0, 10, 5);

        assertEquals(1, productService.getLowStockCount());
    }

    @Test
    void totalInventoryValueSumsPriceTimesQuantity() {
        productService.addProduct("SKU-1", "Widget", "Tools", 10.0, 2, 5);
        productService.addProduct("SKU-2", "Gadget", "Tools", 20.0, 3, 5);

        assertEquals(80.0, productService.getTotalInventoryValue());
    }

    @Test
    void addProductRejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> productService.addProduct("SKU-1", "Widget", "Tools", -1.0, 2, 5));
    }
}
