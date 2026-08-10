package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;

import java.util.List;

public interface PurchaseOrderRepository {
    List<PurchaseOrder> findAll();

    void save(PurchaseOrder purchaseOrder);
}
