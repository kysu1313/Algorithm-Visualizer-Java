package models;

import javafx.animation.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class BubbleSort {

    private int numBars;
    private ObservableList<MyRectangle> rectangles;
    private MyRectangle[] rectArr;
    private AnchorPane grid;
    private ArrayList<Transition> transitions;
    private ArrayList<Transition> fadeTransitions;
    private int duration;

    public BubbleSort(MyRectangle[] _rectArr, AnchorPane _grid) {
        this.grid = _grid;
        this.transitions = new ArrayList<>();
        this.rectArr = _rectArr;
        this.duration = MyRectangle.getDuration();
    }

    public synchronized void sort() {

        SequentialTransition seqT = new SequentialTransition ();
        SequentialTransition seqFade = new SequentialTransition ();
        ParallelTransition parT = new ParallelTransition();
        boolean didChange = false;
        for (int i = 0; i < this.rectArr.length; i++) {
            didChange = false;
                for (int j = 0; j < this.rectArr.length-i-1; j++) {
                    if (this.rectArr[j+1].getHeight() < this.rectArr[j].getHeight()) {
                        this.transitions.add(SortRectangles.swapRects(this.rectArr, j, j+1, this.duration));
                        didChange = true;
                    }
                }
                if (!didChange) break;
        }
        seqT.getChildren().addAll(this.transitions);
        seqT.play();
    }

    private void fadeTransitions(SequentialTransition _seqT) {
        _seqT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evt) {
                ArrayList<FadeTransition> fts = new ArrayList<>();

                for (MyRectangle rect : rectArr) {
                    FadeTransition ft = new FadeTransition();
                    ft.setToValue(0.3);
                    ft.setCycleCount(4);
                    fts.add(ft);
                }
                SequentialTransition seq = new SequentialTransition ();
                seq.getChildren().addAll(fts);
                seq.play();
            }
        });
    }

}
