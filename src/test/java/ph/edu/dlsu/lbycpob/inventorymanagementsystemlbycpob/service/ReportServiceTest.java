package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryProductRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryStockMovementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportServiceTest {
    @Test
    void productsWithMoreOutMovementRankHigher() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        ProductService productService = new ProductService(productRepository);
        StockMovementService stockMovementService = new StockMovementService(new InMemoryStockMovementRepository());
        ReportService reportService = new ReportService(productService, stockMovementService);

        Product fastMover = productService.addProduct("SKU-1", "Widget", "Tools", 10.0, 100, 5);
        Product slowMover = productService.addProduct("SKU-2", "Gadget", "Tools", 10.0, 100, 5);
        stockMovementService.record(fastMover, MovementType.OUT, 20, "SO-1");
        stockMovementService.record(slowMover, MovementType.OUT, 2, "SO-2");

        List<ReportService.ProductMovementSummary> summary = reportService.getMovementSummary();

        assertEquals(fastMover, summary.get(0).product());
        assertEquals(slowMover, summary.get(1).product());
    }
}
