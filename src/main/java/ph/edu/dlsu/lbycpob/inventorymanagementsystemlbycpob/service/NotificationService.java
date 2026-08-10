package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;

public class NotificationService {
    public boolean isLowStock(Product product) {
        return product.getQuantity() <= product.getReorderThreshold();
    }
}