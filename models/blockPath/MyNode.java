package models.blockPath;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MyNode extends Rectangle {

    private double x;
    private double y;
    private double width;
    private double height;
    private boolean isWall;
    private boolean isVisited;
    private double fCost;
    private double gCost;
    private double hCost;
    private double distance;
    private MyNode parent;
    private Color color;

    public MyNode(double _x, double _y, double _width, double _height) {
        super(_x, _y, _width, _height);
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = this.width; // squares
        this.isWall = false;
        this.isVisited = false;
        this.parent = null;
        this.fCost = 0;
        this.gCost = 0;
        this.hCost = 0;
        this.distance = 0;
        setNodeBackgroundColor("darkgray", "black");
        this.color = Color.WHITE;
    }

    public void setNodeBackgroundColor(String _color, String _borderColor) {
        this.setStyle("-fx-fill: " + _color + "; -fx-stroke: black; -fx-stroke-width: 2;");
    }

    public MyNode getMyParent() {
        return parent;
    }

    public void setParent(MyNode parent) {
        this.parent = parent;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public double getfCost() {
        return fCost;
    }

    public void setfCost(double fCost) {
        this.fCost = fCost;
    }

    public double getgCost() {
        return gCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
