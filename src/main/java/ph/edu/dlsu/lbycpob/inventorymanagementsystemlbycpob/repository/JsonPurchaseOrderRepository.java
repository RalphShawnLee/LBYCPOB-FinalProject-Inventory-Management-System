package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.OrderStatus;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonPurchaseOrderRepository implements PurchaseOrderRepository {
    private final Path file;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    public JsonPurchaseOrderRepository(String filePath, ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.file = Path.of(filePath);
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        load();
    }

    @Override
    public List<PurchaseOrder> findAll() {
        return purchaseOrders;
    }

    @Override
    public void save(PurchaseOrder purchaseOrder) {
        purchaseOrders.add(purchaseOrder);
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
                Supplier supplier = findSupplier(JsonUtil.extractString(obj, "supplier"));
                if (supplier == null) {
                    continue;
                }
                List<PurchaseOrderLine> lines = new ArrayList<>();
                for (String lineObj : JsonUtil.extractArray(obj, "lines")) {
                    Product product = findProduct(JsonUtil.extractString(lineObj, "productSku"));
                    if (product == null) {
                        continue;
                    }
                    lines.add(new PurchaseOrderLine(product, JsonUtil.extractInt(lineObj, "quantity")));
                }
                int id = JsonUtil.extractInt(obj, "id");
                maxId = Math.max(maxId, id);
                purchaseOrders.add(new PurchaseOrder(
                        id,
                        supplier,
                        lines,
                        OrderStatus.valueOf(JsonUtil.extractString(obj, "status")),
                        LocalDateTime.parse(JsonUtil.extractString(obj, "createdAt"))
                ));
            }
            PurchaseOrder.ensureCounterAtLeast(maxId + 1);
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

    private Supplier findSupplier(String name) {
        return supplierRepository.findAll().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void persist() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < purchaseOrders.size(); i++) {
            PurchaseOrder o = purchaseOrders.get(i);
            sb.append("  {\"id\":").append(o.getId())
                    .append(",\"supplier\":\"").append(JsonUtil.escape(o.getSupplier().getName()))
                    .append("\",\"status\":\"").append(o.getStatus())
                    .append("\",\"createdAt\":\"").append(o.getCreatedAt())
                    .append("\",\"lines\":[");
            List<PurchaseOrderLine> lines = o.getLines();
            for (int j = 0; j < lines.size(); j++) {
                PurchaseOrderLine line = lines.get(j);
                sb.append("{\"productSku\":\"").append(JsonUtil.escape(line.getProduct().getSku()))
                        .append("\",\"quantity\":").append(line.getQuantity())
                        .append("}");
                if (j < lines.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]}");
            sb.append(i < purchaseOrders.size() - 1 ? ",\n" : "\n");
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
