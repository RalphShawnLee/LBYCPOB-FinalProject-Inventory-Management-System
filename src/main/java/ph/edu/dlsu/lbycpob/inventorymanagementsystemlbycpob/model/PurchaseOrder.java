package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchaseOrder {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final int id;
    private final Supplier supplier;
    private final List<PurchaseOrderLine> lines;
    private final LocalDateTime createdAt;
    private OrderStatus status;

    public PurchaseOrder(Supplier supplier, List<PurchaseOrderLine> lines) {
        this.id = COUNTER.getAndIncrement();
        this.supplier = supplier;
        this.lines = lines;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public PurchaseOrder(int id, Supplier supplier, List<PurchaseOrderLine> lines, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.supplier = supplier;
        this.lines = lines;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static void ensureCounterAtLeast(int value) {
        COUNTER.updateAndGet(current -> Math.max(current, value));
    }

    public int getId() {
        return id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public List<PurchaseOrderLine> getLines() {
        return lines;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PO-" + id + " (" + status + ")";
    }
}
