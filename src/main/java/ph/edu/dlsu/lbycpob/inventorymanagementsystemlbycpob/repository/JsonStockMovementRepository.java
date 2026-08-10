package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.StockMovement;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonStockMovementRepository implements StockMovementRepository {
    private final Path file;
    private final ProductRepository productRepository;
    private final List<StockMovement> stockMovements = new ArrayList<>();

    public JsonStockMovementRepository(String filePath, ProductRepository productRepository) {
        this.file = Path.of(filePath);
        this.productRepository = productRepository;
        load();
    }

    @Override
    public List<StockMovement> findAll() {
        return stockMovements;
    }

    @Override
    public void save(StockMovement stockMovement) {
        stockMovements.add(stockMovement);
        persist();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            String content = Files.readString(file);
            for (String obj : JsonUtil.splitObjects(content)) {
                Product product = findProduct(JsonUtil.extractString(obj, "productSku"));
                if (product == null) {
                    continue;
                }
                stockMovements.add(new StockMovement(
                        product,
                        MovementType.valueOf(JsonUtil.extractString(obj, "type")),
                        JsonUtil.extractInt(obj, "quantity"),
                        JsonUtil.extractString(obj, "reference"),
                        LocalDateTime.parse(JsonUtil.extractString(obj, "timestamp"))
                ));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Product findProduct(String sku) {
        return productRepository.findAll().stream()
                .filter(p -> p.getSku().equals(sku))
                .findFirst()
                .orElse(null);
    }

    private void persist() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < stockMovements.size(); i++) {
            StockMovement m = stockMovements.get(i);
            sb.append("  {\"productSku\":\"").append(JsonUtil.escape(m.getProduct().getSku()))
                    .append("\",\"type\":\"").append(m.getType())
                    .append("\",\"quantity\":").append(m.getQuantity())
                    .append(",\"reference\":\"").append(JsonUtil.escape(m.getReference()))
                    .append("\",\"timestamp\":\"").append(m.getTimestamp())
                    .append("\"}");
            sb.append(i < stockMovements.size() - 1 ? ",\n" : "\n");
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
