package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.StockMovement;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStockMovementRepository implements StockMovementRepository {
    private final List<StockMovement> stockMovements = new ArrayList<>();

    @Override
    public List<StockMovement> findAll() {
        return stockMovements;
    }

    @Override
    public void save(StockMovement stockMovement) {
        stockMovements.add(stockMovement);
    }
}
