package models;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static models.MyRectangle.colorRect;

public class SortRectangles {

    private int numBars;
    private ObservableList<MyRectangle> rectangles;
    private MyRectangle[] rectArr;
    private AnchorPane grid;
    private static double SPACING;
    private static final int BARS = 35;
    private static final int BASE_HEIGHT = 10;
    private static int RECT_WIDTH = 3; // 15
    private int duration;

    public SortRectangles(int _numBars, AnchorPane _grid) {
        this.rectangles = FXCollections.observableArrayList();
        this.rectArr = new MyRectangle[_numBars];
        this.grid = _grid;
        SPACING = this.grid.getWidth() / _numBars;
        this.duration = MyRectangle.getDuration();
        createBaseLine(this.grid);
        for (int i = 0; i < _numBars; i++) {
            this.rectangles.add(createRect(i, Color.BLACK));
            this.rectArr[i] = createRect(i, Color.BLACK);
        }
    }

    public SortRectangles(AnchorPane _grid) {
        this.rectangles = FXCollections.observableArrayList();
        this.rectArr = new MyRectangle[BARS];
        this.grid = _grid;
        this.duration = MyRectangle.getDuration();
        createBaseLine(this.grid);
        SPACING = (this.grid.getWidth() / 50) + 5;
        for (int i = 0; i < BARS; i++) {
            this.rectangles.add(createRect(i, Color.BLACK));
            this.rectArr[i] = createRect(i, Color.BLACK);
        }
    }

    /**
     * Swap locations of elements in _list at indices i and j and return the transition.
     * @param _list
     * @param _i
     * @param _j
     * @return
     */
    public static ParallelTransition swapRects(MyRectangle[] _list, int _i, int _j, int _duration) {

        // Add all transitions together.
        ParallelTransition pTran = new ParallelTransition();
        // The distance to move a rectangle is calculated from the distance between their indices.
        int move = _j - _i;
        // Add the transitions for both rectangles
        pTran.getChildren().addAll(_list[_i].setToX(move * SPACING), _list[_j].setToX(-(SPACING * move)));
        // Swap their locations in the array
        MyRectangle tmp = _list[_i];
        _list[_i] = _list[_j];
        _list[_j] = tmp;
        // Update the color once they have been visited
        pTran.getChildren().addAll(colorRect(_list[_i], Color.BLUE, _duration), colorRect(_list[_j], Color.BLUE, _duration));
        return pTran;
    }

    public static ParallelTransition colorRect(List<MyRectangle> _list, Color _color, int _duration) {
        ParallelTransition pTran = new ParallelTransition();
        for (MyRectangle rect : _list) {
            FillTransition fill = new FillTransition();
            fill.setShape(rect);
            fill.setToValue(_color);
            fill.setDuration(Duration.millis(rect.getDuration()));
            pTran.getChildren().add(fill);
        }
        return pTran;
    }

    public static ParallelTransition colorRect(MyRectangle[] _list, Color _color, int _duration, int...k) {
        ParallelTransition pTran = new ParallelTransition();
        for (int i = 0; i < k.length; i++) {
            FillTransition fill = new FillTransition();
            fill.setShape(_list[k[i]]);
            fill.setToValue(_color);
            fill.setDuration(Duration.millis(_duration)); // 100
            pTran.getChildren().add(fill);
        }
        return pTran;
    }

    public static ParallelTransition colorRect(MyRectangle _rect, Color _color, int _duration) {
        ParallelTransition pTran = new ParallelTransition();
        FillTransition fill = new FillTransition();
        fill.setShape(_rect);
        fill.setToValue(_color);
        fill.setDuration(Duration.millis(_duration));
        pTran.getChildren().add(fill);
        return pTran;
    }

    /**
     * Create a single rectangle at _num indices.
     * @param _num
     * @param _color
     * @return
     */
    private MyRectangle createRect(int _num, Color _color) {
        MyRectangle rect = new MyRectangle();
        rect.setX((SPACING * _num) + SPACING);
        rect.setY(BASE_HEIGHT); //  (Math.random()*500)+1
        rect.setHeight((Math.random()*(this.grid.getHeight()-20))+BASE_HEIGHT);
        RECT_WIDTH = (int)SPACING-1;
        rect.setWidth(RECT_WIDTH);
        rect.setFill(_color);
        return rect;
    }

    /**
     * Create a static baseline rectangle so it looks like the other rectangles aren't floating.
     * @param _grid
     */
    public static void createBaseLine(AnchorPane _grid) {
        MyRectangle baseLine = new MyRectangle();
        baseLine.setX(0.0);
        baseLine.setY(0.0);
        baseLine.setWidth(_grid.getWidth());
        baseLine.setHeight(BASE_HEIGHT);
        _grid.getChildren().add(baseLine);
    }

    private void draggable(MyRectangle _rect) {
        _rect.setOnMousePressed((t) -> {
            double dragX = t.getSceneX();
            double dragY = t.getSceneY();

            MyRectangle r = (MyRectangle) (t.getSource());
            _rect.toFront();
        });
//        _rect.setOnDragDetected();
    }

    public MyRectangle[] getRectArr() {
        return this.rectArr;
    }

    public ObservableList<MyRectangle> getRectangles() {
        return this.rectangles;
    }

}
