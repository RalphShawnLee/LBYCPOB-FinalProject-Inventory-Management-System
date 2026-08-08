package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;

public class Product {
    private String sku;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private int reorderThreshold;

    public Product(String sku, String name, String category, double price, int quantity, int reorderThreshold) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(int reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    @Override
    public String toString() {
        return name + " (" + sku + ")";
    }
}
