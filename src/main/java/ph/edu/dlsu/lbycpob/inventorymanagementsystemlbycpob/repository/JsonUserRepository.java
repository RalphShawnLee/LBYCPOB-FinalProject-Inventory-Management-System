package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.User;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.UserRole;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonUserRepository implements UserRepository {
    private final Path file;
    private final List<User> users = new ArrayList<>();

    public JsonUserRepository(String filePath) {
        this.file = Path.of(filePath);
        load();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
        persist();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            String content = Files.readString(file);
            for (String obj : JsonUtil.splitObjects(content)) {
                users.add(new User(
                        JsonUtil.extractString(obj, "username"),
                        JsonUtil.extractString(obj, "passwordHash"),
                        UserRole.valueOf(JsonUtil.extractString(obj, "role"))
                ));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void persist() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            sb.append("  {\"username\":\"").append(JsonUtil.escape(u.getUsername()))
                    .append("\",\"passwordHash\":\"").append(JsonUtil.escape(u.getPasswordHash()))
                    .append("\",\"role\":\"").append(u.getRole())
                    .append("\"}");
            sb.append(i < users.size() - 1 ? ",\n" : "\n");
        }
        sb.append("]\n");
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, sb.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
