package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.MovementType;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.OrderStatus;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemorySalesOrderRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryStockMovementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalesOrderServiceTest {
    @Test
    void fulfillingOrderDecreasesStockAndLogsOutMovement() {
        StockMovementService stockMovementService = new StockMovementService(new InMemoryStockMovementRepository());
        SalesOrderService salesOrderService = new SalesOrderService(new InMemorySalesOrderRepository(), stockMovementService);
        Product product = new Product("SKU-1", "Widget", "Tools", 10.0, 5, 3);

        SalesOrder order = salesOrderService.createOrder(List.of(new SalesOrderLine(product, 2)));
        salesOrderService.fulfillOrder(order);

        assertEquals(3, product.getQuantity());
        assertEquals(OrderStatus.FULFILLED, order.getStatus());
        assertEquals(MovementType.OUT, stockMovementService.getAll().get(0).getType());
    }

    @Test
    void fulfillingWithInsufficientStockThrowsAndLeavesQuantityUnchanged() {
        StockMovementService stockMovementService = new StockMovementService(new InMemoryStockMovementRepository());
        SalesOrderService salesOrderService = new SalesOrderService(new InMemorySalesOrderRepository(), stockMovementService);
        Product product = new Product("SKU-1", "Widget", "Tools", 10.0, 1, 3);

        SalesOrder order = salesOrderService.createOrder(List.of(new SalesOrderLine(product, 5)));

        assertThrows(IllegalStateException.class, () -> salesOrderService.fulfillOrder(order));
        assertEquals(1, product.getQuantity());
        assertEquals(0, stockMovementService.getAll().size());
    }
}
