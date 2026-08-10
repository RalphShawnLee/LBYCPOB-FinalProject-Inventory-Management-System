package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPurchaseOrderRepository implements PurchaseOrderRepository {
    private final List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    @Override
    public List<PurchaseOrder> findAll() {
        return purchaseOrders;
    }

    @Override
    public void save(PurchaseOrder purchaseOrder) {
        purchaseOrders.add(purchaseOrder);
    }
}
