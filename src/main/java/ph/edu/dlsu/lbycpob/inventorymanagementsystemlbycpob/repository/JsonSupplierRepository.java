package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;

import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonSupplierRepository implements SupplierRepository {
    private final Path file;
    private final List<Supplier> suppliers = new ArrayList<>();

    public JsonSupplierRepository(String filePath) {
        this.file = Path.of(filePath);
        load();
    }

    @Override
    public List<Supplier> findAll() {
        return suppliers;
    }

    @Override
    public void save(Supplier supplier) {
        suppliers.add(supplier);
        persist();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            String content = Files.readString(file);
            for (String obj : JsonUtil.splitObjects(content)) {
                suppliers.add(new Supplier(
                        JsonUtil.extractString(obj, "name"),
                        JsonUtil.extractString(obj, "email"),
                        JsonUtil.extractString(obj, "phone")
                ));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void persist() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier s = suppliers.get(i);
            sb.append("  {\"name\":\"").append(JsonUtil.escape(s.getName()))
                    .append("\",\"email\":\"").append(JsonUtil.escape(s.getEmail()))
                    .append("\",\"phone\":\"").append(JsonUtil.escape(s.getPhone()))
                    .append("\"}");
            sb.append(i < suppliers.size() - 1 ? ",\n" : "\n");
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
