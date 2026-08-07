module ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob {
    requires javafx.controls;
    requires javafx.fxml;


    opens ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller to javafx.fxml;
    exports ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob;
    exports ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;
    exports ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model;
    exports ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service;
    exports ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository;
}