package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrder;

import java.util.ArrayList;
import java.util.List;

public class InMemorySalesOrderRepository implements SalesOrderRepository {
    private final List<SalesOrder> salesOrders = new ArrayList<>();

    @Override
    public List<SalesOrder> findAll() {
        return salesOrders;
    }

    @Override
    public void save(SalesOrder salesOrder) {
        salesOrders.add(salesOrder);
    }
}
