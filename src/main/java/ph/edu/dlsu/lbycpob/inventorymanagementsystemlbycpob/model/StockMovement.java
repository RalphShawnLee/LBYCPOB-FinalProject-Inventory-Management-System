package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;

import java.time.LocalDateTime;

public class StockMovement {
    private final Product product;
    private final MovementType type;
    private final int quantity;
    private final String reference;
    private final LocalDateTime timestamp;
    public StockMovement(Product product, MovementType type, int quantity, String reference) {
        this.product = product;
        this.type = type;
        this.quantity = quantity;
        this.reference = reference;
        this.timestamp = LocalDateTime.now();
    }
    public Product getProduct() {
        return product;
    }

    public MovementType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}