package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SalesOrder {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final int id;
    private final List<SalesOrderLine> lines;
    private final LocalDateTime createdAt;
    private OrderStatus status;

    public SalesOrder(List<SalesOrderLine> lines) {
        this.id = COUNTER.getAndIncrement();
        this.lines = lines;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public int getId() {
        return id;
    }

    public List<SalesOrderLine> getLines() {
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
        return "SO-" + id + " (" + status + ")";
    }
}
