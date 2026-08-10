package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonProductRepository implements ProductRepository {
    private final Path file;
    private final List<Product> products = new ArrayList<>();

    public JsonProductRepository(String filePath) {
        this.file = Path.of(filePath);
        load();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public void save(Product product) {
        products.add(product);
        persist();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            String content = Files.readString(file);
            for (String obj : JsonUtil.splitObjects(content)) {
                products.add(new Product(
                        JsonUtil.extractString(obj, "sku"),
                        JsonUtil.extractString(obj, "name"),
                        JsonUtil.extractString(obj, "category"),
                        JsonUtil.extractDouble(obj, "price"),
                        JsonUtil.extractInt(obj, "quantity"),
                        JsonUtil.extractInt(obj, "reorderThreshold")
                ));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void persist() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            sb.append("  {\"sku\":\"").append(JsonUtil.escape(p.getSku()))
                    .append("\",\"name\":\"").append(JsonUtil.escape(p.getName()))
                    .append("\",\"category\":\"").append(JsonUtil.escape(p.getCategory()))
                    .append("\",\"price\":").append(p.getPrice())
                    .append(",\"quantity\":").append(p.getQuantity())
                    .append(",\"reorderThreshold\":").append(p.getReorderThreshold())
                    .append("}");
            sb.append(i < products.size() - 1 ? ",\n" : "\n");
        }
        sb.append("]\n");
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, sb.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
