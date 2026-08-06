module ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob {
    requires javafx.controls;
    requires javafx.fxml;


    opens ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob to javafx.fxml;
    exports ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob;
}