package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationServiceTest {
    private final NotificationService notificationService = new NotificationService();

    @Test
    void isLowStockTrueAtOrBelowThreshold() {
        Product product = new Product("SKU-1", "Widget", "Tools", 10.0, 3, 3);
        assertTrue(notificationService.isLowStock(product));
    }

    @Test
    void isLowStockFalseAboveThreshold() {
        Product product = new Product("SKU-1", "Widget", "Tools", 10.0, 10, 3);
        assertFalse(notificationService.isLowStock(product));
    }
}
