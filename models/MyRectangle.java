package models;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class MyRectangle extends Rectangle {

    private double xPos;
    private double yPos;
    private double softX;
    private double softY;
    private double width;
    private double height;
    private double dragX;
    private double dragY;
    private Color color;

    // Animation Duration
    private static int duration = 600;
    private int dura;

    public MyRectangle(Double _xPos, Double _yPos,  Double _softX, Double _softY, Double _width, Double _height, Color _color) {
        super(_xPos, _yPos, _width, _height);
        this.xPos = _xPos;
        this.yPos = _yPos;
        this.softX = _softX;
        this.softY = _softY;
        this.width = _width;
        this.height = _height;
        this.dragX = 0;
        this.dragY = 0;
        this.color = _color;
        this.dura = 100;
    }

    public MyRectangle() {

    }

    public TranslateTransition setToX(Double _x) {
        TranslateTransition t = new TranslateTransition();
        t.setNode(this);
        t.setDuration(Duration.millis(duration));
        t.setByX(_x);

        return t;
    }

    public static ParallelTransition colorRect(Color _color, MyRectangle _rect) {
        ParallelTransition pt = new ParallelTransition();
        FillTransition fill = new FillTransition();
        fill.setShape(_rect);
        fill.setDuration(Duration.millis(duration));
        fill.setToValue(_color);
        pt.getChildren().add(fill);
        return pt;
    }

    public static ParallelTransition colorRect(Color _color, MyRectangle[] _arr) {
        ParallelTransition pt = new ParallelTransition();
        for (int i = 0; i < _arr.length; i++) {
            FillTransition fill = new FillTransition();
            fill.setShape(_arr[i]);
            fill.setDuration(Duration.millis(duration));
            fill.setToValue(_color);
            pt.getChildren().add(fill);
        }
        return pt;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setFill(color);
    }

    public int getDura() {
        return dura;
    }

    public void setDura(int dura) {
        this.dura = dura;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getSoftX() {
        return softX;
    }

    public void setSoftX(double softX) {
        this.softX = softX;
    }

    public double getSoftY() {
        return softY;
    }

    public void setSoftY(double softY) {
        this.softY = softY;
    }

    public static int getDuration() {
        return duration;
    }

    public static void setDuration(int duration) {
        MyRectangle.duration = duration;
    }
}
