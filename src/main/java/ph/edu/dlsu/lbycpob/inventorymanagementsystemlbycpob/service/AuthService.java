package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.User;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.UserRole;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.UserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        if (userRepository.findByUsername("admin").isEmpty()) {
            // ponytail: dev-only seeded credential, real user management out of scope
            userRepository.save(new User("admin", hash("admin123"), UserRole.ADMIN));
        }
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPasswordHash().equals(hash(password)));
    }

    private static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
