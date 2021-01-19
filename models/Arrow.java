package models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;

public class Arrow extends Group {

    private Polyline mainLine = new Polyline();
    private Polyline headA = new Polyline();
    private Polyline headB = new Polyline();
    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private SimpleBooleanProperty headAVisible = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty headBVisible = new SimpleBooleanProperty(true);
    private final double ARROW_SCALER = 20;
    private final double ARROWHEAD_ANGLE = Math.toRadians(20);
    private final double ARROWHEAD_LENGTH = 10;

    public Arrow(double x1, double x2, double y1, double y2) {
        this.x1.set(x1);
        this.x2.set(x2);
        this.y1.set(y1);
        this.y2.set(y2);

        this.getChildren().addAll(this.mainLine, this.headA, this.headB);

        for (SimpleDoubleProperty s: new SimpleDoubleProperty[]{this.x1, this.y1, this.x2, this.y2}) {
            s.addListener((l, o, n) -> update());
        }
        setUpStyleClassStructure();

        headA.visibleProperty().bind(headAVisible);
        headB.visibleProperty().bind(headBVisible);
        update();
    }

    private void setUpStyleClassStructure() {
        this.mainLine.getStyleClass().setAll("arrow");
        this.headA.getStyleClass().setAll("arrow");
        this.headB.getStyleClass().setAll("arrow");

        this.headA.getStyleClass().add("arrowHead");
        this.headB.getStyleClass().add("arrowHead");

        getStyleClass().addListener((ListChangeListener<? super String>) c -> {
            c.next();
            for (Polyline p : new Polyline[]{this.mainLine, this.headA, this.headB}) {
                p.getStyleClass().addAll(c.getAddedSubList());
                p.getStyleClass().removeAll(c.getRemoved());
            }
        });
    }

    private void update() {
        double[] start = scale(this.x1.get(), this.y1.get(), this.x2.get(), this.y2.get());
        double[] end = scale(this.x2.get(), this.y2.get(), this.x1.get(), this.y1.get());

        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];

        this.mainLine.getPoints().setAll(x1, y1, x2, y2);

        double theta = Math.atan2(y2 - y1, x2 - x1);
        double cosAngPlus = Math.cos(theta + this.ARROWHEAD_ANGLE) * this.ARROWHEAD_LENGTH;
        double sinAngPlus = Math.sin(theta + this.ARROWHEAD_ANGLE) * this.ARROWHEAD_LENGTH;
        double cosAngMinus = Math.cos(theta - this.ARROWHEAD_ANGLE) * this.ARROWHEAD_LENGTH;
        double sinAngMinus = Math.sin(theta - this.ARROWHEAD_ANGLE) * this.ARROWHEAD_LENGTH;

        // Arrowhead 1
        double x = x1 + cosAngPlus;
        double y = y1 + sinAngPlus;
        this.headA.getPoints().setAll(x, y, x1, y1);
        x = x1 + cosAngMinus;
        y = y1 + sinAngMinus;
        this.headA.getPoints().addAll(x, y);


        x = x2 - cosAngPlus;
        y = y2 - sinAngPlus;
        this.headB.getPoints().setAll(x, y, x2, y2);
        x = x2 - cosAngMinus;
        y = y2 - sinAngMinus;
        this.headB.getPoints().addAll(x, y);

    }

    private double[] scale(double x1, double y1, double x2, double y2) {
        double theta = Math.atan2(y2 - y1, x2 - x1);
        return new double[]{
                x1 + Math.cos(theta) * this.ARROW_SCALER,
                y1 + Math.sin(theta) * this.ARROW_SCALER
        };
    }

    public Polyline getMainLine() {
        return this.mainLine;
    }

    public void setMainLine(Polyline mainLine) {
        this.mainLine = mainLine;
    }

    public boolean isHeadAVisible() {
        return headAVisible.get();
    }

    public SimpleBooleanProperty headAVisibleProperty() {
        return headAVisible;
    }

    public void setHeadAVisible(boolean headAVisible) {
        this.headAVisible.set(headAVisible);
    }

    public boolean isHeadBVisible() {
        return headBVisible.get();
    }

    public SimpleBooleanProperty headBVisibleProperty() {
        return headBVisible;
    }

    public void setHeadBVisible(boolean headBVisible) {
        this.headBVisible.set(headBVisible);
    }

    public double getX1() {
        return this.x1.get();
    }

    public SimpleDoubleProperty x1Property() {
        return this.x1;
    }

    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public double getX2() {
        return this.x2.get();
    }

    public SimpleDoubleProperty x2Property() {
        return this.x2;
    }

    public void setX2(double x2) {
        this.x2.set(x2);
    }

    public double getY1() {
        return this.y1.get();
    }

    public SimpleDoubleProperty y1Property() {
        return this.y1;
    }

    public void setY1(double y1) {
        this.y1.set(y1);
    }

    public double getY2() {
        return this.y2.get();
    }

    public SimpleDoubleProperty y2Property() {
        return this.y2;
    }

    public void setY2(double y2) {
        this.y2.set(y2);
    }


}
