package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import org.junit.jupiter.api.Test;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryUserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {
    @Test
    void loginSucceedsWithSeededAdminCredentials() {
        AuthService authService = new AuthService(new InMemoryUserRepository());

        assertTrue(authService.login("admin", "admin123").isPresent());
    }

    @Test
    void loginFailsWithWrongPassword() {
        AuthService authService = new AuthService(new InMemoryUserRepository());

        assertFalse(authService.login("admin", "wrong").isPresent());
    }
}
