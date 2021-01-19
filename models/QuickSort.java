package models;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static models.SortRectangles.colorRect;
import static models.SortRectangles.swapRects;

public class QuickSort {

    private AnchorPane grid;
    private int numBars;
    private MyRectangle[] rectArr;
    private ArrayList<Transition> transitions;
    private ArrayList<Transition> fadeTransitions;
    private SequentialTransition seqT;
    private int duration;

    public QuickSort(MyRectangle[] _rectArr, AnchorPane _grid) {
        this.transitions = new ArrayList<>();
        this.grid = _grid;
        this.rectArr = _rectArr;
        this.duration = MyRectangle.getDuration();
        this.seqT = new SequentialTransition();
        if (this.rectArr == null || this.rectArr.length == 0) {
            return;
        }
        sort(0, this.rectArr.length-1);
        this.seqT.getChildren().addAll(this.transitions);
        this.seqT.play();
//        sort(this.rectArr, 0, this.rectArr.length-1);
    }

    private void sort(int low, int high) {
        int i = low, j = high;

        MyRectangle pivot = this.rectArr[low + (high-low)/2];
        this.transitions.add(SortRectangles.colorRect(pivot, Color.RED, this.duration));

        while (i <= j) {
            while (this.rectArr[i].getHeight() < pivot.getHeight()) {
                i++;
            }
            while (this.rectArr[j].getHeight() > pivot.getHeight()) {
                j--;
            }

            if (i <= j) {
                this.transitions.add(SortRectangles.swapRects(this.rectArr, j, i, this.duration));
                i++;
                j--;
            }
        }

        if (low < j) {
            sort(low, j);
        }
        if (i < high) {
            sort(i, high);
        }
    }

}
