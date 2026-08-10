package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.StockMovement;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.StockMovementRepository;

import java.util.Comparator;
import java.util.List;

public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    public void record(Product product, MovementType type, int quantity, String reference) {
        stockMovementRepository.save(new StockMovement(product, type, quantity, reference));
    }

    public List<StockMovement> getAll() {
        return stockMovementRepository.findAll();
    }

    public List<StockMovement> getRecent(int n) {
        return stockMovementRepository.findAll().stream()
                .sorted(Comparator.comparing(StockMovement::getTimestamp).reversed())
                .limit(n)
                .toList();
    }
}