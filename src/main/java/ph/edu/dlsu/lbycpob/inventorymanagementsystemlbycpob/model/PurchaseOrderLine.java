package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;

public class PurchaseOrderLine {
    private final Product product;
    private final int quantity;

    public PurchaseOrderLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x " + quantity;
    }
}