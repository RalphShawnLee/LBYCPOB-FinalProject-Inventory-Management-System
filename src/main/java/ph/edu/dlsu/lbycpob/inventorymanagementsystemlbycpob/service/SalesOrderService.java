package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.OrderStatus;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.SalesOrderRepository;

import java.util.List;

public class SalesOrderService {
    private final SalesOrderRepository salesOrderRepository;
    private final StockMovementService stockMovementService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository, StockMovementService stockMovementService) {
        this.salesOrderRepository = salesOrderRepository;
        this.stockMovementService = stockMovementService;
    }

    public List<SalesOrder> getAll() {
        return salesOrderRepository.findAll();
    }

    public SalesOrder createOrder(List<SalesOrderLine> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one line");
        }
        SalesOrder order = new SalesOrder(lines);
        salesOrderRepository.save(order);
        return order;
    }

    public void fulfillOrder(SalesOrder order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not pending");
        }
        for (SalesOrderLine line : order.getLines()) {
            if (line.getProduct().getQuantity() < line.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for " + line.getProduct().getName());
            }
        }
        for (SalesOrderLine line : order.getLines()) {
            Product product = line.getProduct();
            product.setQuantity(product.getQuantity() - line.getQuantity());
            stockMovementService.record(product, MovementType.OUT, line.getQuantity(), "SO-" + order.getId());
        }
        order.setStatus(OrderStatus.FULFILLED);
    }
}