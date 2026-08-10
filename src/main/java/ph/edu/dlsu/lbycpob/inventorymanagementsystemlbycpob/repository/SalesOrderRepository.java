package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrder;

import java.util.List;

public interface SalesOrderRepository {
    List findAll();

    void save(SalesOrder salesOrder);
}
