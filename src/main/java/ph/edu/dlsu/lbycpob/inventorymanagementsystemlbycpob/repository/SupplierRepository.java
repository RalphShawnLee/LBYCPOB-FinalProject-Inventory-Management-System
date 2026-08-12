package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.util.List;

public interface SupplierRepository {
    List<Supplier> findAll();

    void save(Supplier supplier);

    void update(Supplier supplier);
}
