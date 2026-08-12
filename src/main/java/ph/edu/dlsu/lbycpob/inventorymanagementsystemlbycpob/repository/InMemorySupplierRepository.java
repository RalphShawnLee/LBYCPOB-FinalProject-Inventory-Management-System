package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class InMemorySupplierRepository implements SupplierRepository {
    private final List<Supplier> suppliers = new ArrayList<>();

    @Override
    public List<Supplier> findAll() {
        return suppliers;
    }

    @Override
    public void save(Supplier supplier) {
        suppliers.add(supplier);
    }

    @Override
    public void update(Supplier supplier) {
    }
}
