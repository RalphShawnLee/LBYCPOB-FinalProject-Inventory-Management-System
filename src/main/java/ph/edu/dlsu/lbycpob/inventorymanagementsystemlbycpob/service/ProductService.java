package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.ProductRepository;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product addProduct(String sku, String name, String category, double price, int quantity, int reorderThreshold) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (reorderThreshold < 0) {
            throw new IllegalArgumentException("Reorder threshold cannot be negative");
        }
        Product product = new Product(sku, name, category, price, quantity, reorderThreshold);
        productRepository.save(product);
        return product;
    }

    public void updateProduct(Product product, String sku, String name, String category, double price, int quantity, int reorderThreshold) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (reorderThreshold < 0) {
            throw new IllegalArgumentException("Reorder threshold cannot be negative");
        }
        product.setSku(sku);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setReorderThreshold(reorderThreshold);
        productRepository.update(product);
    }

    public long getLowStockCount() {
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() <= product.getReorderThreshold())
                .count();
    }

    public double getTotalInventoryValue() {
        return productRepository.findAll().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }
}
