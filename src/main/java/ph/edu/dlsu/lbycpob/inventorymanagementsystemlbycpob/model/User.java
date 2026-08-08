package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;

public class User {
    private final String username;
    private final String passwordHash;
    private final UserRole role;

    public User(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }
}
