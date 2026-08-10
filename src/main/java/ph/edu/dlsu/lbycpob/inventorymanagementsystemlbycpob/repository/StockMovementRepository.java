package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.StockMovement;

import java.util.List;

public interface StockMovementRepository {
    List<StockMovement> findAll();

    void save(StockMovement stockMovement);
}
