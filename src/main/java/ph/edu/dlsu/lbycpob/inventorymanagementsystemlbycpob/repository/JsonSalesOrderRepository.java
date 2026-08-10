package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.OrderStatus;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrderLine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonSalesOrderRepository implements SalesOrderRepository {
    private final Path file;
    private final ProductRepository productRepository;
    private final List<SalesOrder> salesOrders = new ArrayList<>();

    public JsonSalesOrderRepository(String filePath, ProductRepository productRepository) {
        this.file = Path.of(filePath);
        this.productRepository = productRepository;
        load();
    }

    @Override
    public List<SalesOrder> findAll() {
        return salesOrders;
    }

    @Override
    public void save(SalesOrder salesOrder) {
        salesOrders.add(salesOrder);
        persist();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            String content = Files.readString(file);
            int maxId = 0;
            for (String obj : JsonUtil.splitObjects(content)) {
                List<SalesOrderLine> lines = new ArrayList<>();
                for (String lineObj : JsonUtil.extractArray(obj, "lines")) {
                    Product product = findProduct(JsonUtil.extractString(lineObj, "productSku"));
                    if (product == null) {
                        continue;
                    }
                    lines.add(new SalesOrderLine(product, JsonUtil.extractInt(lineObj, "quantity")));
                }
                int id = JsonUtil.extractInt(obj, "id");
                maxId = Math.max(maxId, id);
                salesOrders.add(new SalesOrder(
                        id,
                        lines,
                        OrderStatus.valueOf(JsonUtil.extractString(obj, "status")),
                        LocalDateTime.parse(JsonUtil.extractString(obj, "createdAt"))
                ));
            }
            SalesOrder.ensureCounterAtLeast(maxId + 1);
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
        for (int i = 0; i < salesOrders.size(); i++) {
            SalesOrder o = salesOrders.get(i);
            sb.append("  {\"id\":").append(o.getId())
                    .append(",\"status\":\"").append(o.getStatus())
                    .append("\",\"createdAt\":\"").append(o.getCreatedAt())
                    .append("\",\"lines\":[");
            List<SalesOrderLine> lines = o.getLines();
            for (int j = 0; j < lines.size(); j++) {
                SalesOrderLine line = lines.get(j);
                sb.append("{\"productSku\":\"").append(JsonUtil.escape(line.getProduct().getSku()))
                        .append("\",\"quantity\":").append(line.getQuantity())
                        .append("}");
                if (j < lines.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]}");
            sb.append(i < salesOrders.size() - 1 ? ",\n" : "\n");
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
