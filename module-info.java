module AlgoViz {
    requires javafx.fxml;
    requires javafx.controls;

    opens models.blockPath;
    opens models;
    opens controllers;
    opens sample;
}