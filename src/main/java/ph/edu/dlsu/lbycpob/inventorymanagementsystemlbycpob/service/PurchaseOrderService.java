package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.OrderStatus;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.PurchaseOrderRepository;

import java.util.List;

public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final StockMovementService stockMovementService;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, StockMovementService stockMovementService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.stockMovementService = stockMovementService;
    }

    public List<PurchaseOrder> getAll() {
        return purchaseOrderRepository.findAll();
    }

    public PurchaseOrder createOrder(Supplier supplier, List<PurchaseOrderLine> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one line");
        }
        PurchaseOrder order = new PurchaseOrder(supplier, lines);
        purchaseOrderRepository.save(order);
        return order;
    }

    public void receiveOrder(PurchaseOrder order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order is not pending");
        }
        for (PurchaseOrderLine line : order.getLines()) {
            Product product = line.getProduct();
            product.setQuantity(product.getQuantity() + line.getQuantity());
            stockMovementService.record(product, MovementType.IN, line.getQuantity(), "PO-" + order.getId());
        }
        order.setStatus(OrderStatus.RECEIVED);
    }
}
