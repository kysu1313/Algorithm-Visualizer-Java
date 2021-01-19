package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.Map;

public class Vertex extends Button {

    private static int count = 0;
    private int ID;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();
    private Map<Vertex, Number> neighbors;
    private double x;
    private double y;

    public Vertex(Double _x, Double _y) {
        this.neighbors = new HashMap<>();
        this.x = _x;
        this.y = _y;
        setLayoutX(this.x);
        setLayoutY(this.y);

        this.translateXProperty().bind(this.widthProperty().divide(-2));
        this.translateYProperty().bind(this.heightProperty().divide(-2));

        this.ID = count++;
        setText(this.ID + "");
        getStyleClass().add("visNode");
    }

    public void addNeighbor(Vertex _vertex) {
        double distance = Math.abs(Math.pow((_vertex.getX() - this.getX()), 2)) + Math.abs(Math.pow((_vertex.getY() - this.getY()), 2));
        System.out.println("distance: " + distance);
        this.neighbors.put(_vertex, distance);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int _count) {
        count = _count;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
