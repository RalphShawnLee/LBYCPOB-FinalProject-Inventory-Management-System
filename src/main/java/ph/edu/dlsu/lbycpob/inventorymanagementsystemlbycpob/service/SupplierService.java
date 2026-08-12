package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.SupplierRepository;

import java.util.List;

public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAll() {
        return supplierRepository.findAll();
    }

    public Supplier addSupplier(String name, String email, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name is required");
        }
        Supplier supplier = new Supplier(name, email, phone);
        supplierRepository.save(supplier);
        return supplier;
    }

    public void updateSupplier(Supplier supplier, String name, String email, String phone) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Supplier name is required");
        }
        supplier.setName(name);
        supplier.setEmail(email);
        supplier.setPhone(phone);
        supplierRepository.update(supplier);
    }
}
