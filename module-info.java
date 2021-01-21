module AlgoViz {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens models.blockPath;
    opens models;
    opens controllers;
    opens sample;
}