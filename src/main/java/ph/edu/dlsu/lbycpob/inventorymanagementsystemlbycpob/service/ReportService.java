package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.StockMovement;

import java.util.Comparator;
import java.util.List;

public class ReportService {
    private final ProductService productService;
    private final StockMovementService stockMovementService;

    public ReportService(ProductService productService, StockMovementService stockMovementService) {
        this.productService = productService;
        this.stockMovementService = stockMovementService;
    }

    public List<ProductMovementSummary> getMovementSummary() {
        List<StockMovement> movements = stockMovementService.getAll();
        return productService.getAll().stream()
                .map(product -> new ProductMovementSummary(product, totalOutQuantity(movements, product)))
                .sorted(Comparator.comparingInt(ProductMovementSummary::totalOutQuantity).reversed())
                .toList();
    }

    private int totalOutQuantity(List<StockMovement> movements, Product product) {
        return movements.stream()
                .filter(m -> m.getType() == MovementType.OUT && m.getProduct() == product)
                .mapToInt(StockMovement::getQuantity)
                .sum();
    }

    public record ProductMovementSummary(Product product, int totalOutQuantity) {
    }
}