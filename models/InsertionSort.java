package models;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

import static models.SortRectangles.swapRects;

public class InsertionSort {

    private AnchorPane grid;
    private int numBars;
    private MyRectangle[] rectArr;
    private ArrayList<Transition> transitions;
    private int duration;

    public InsertionSort(MyRectangle[] _rectArr, AnchorPane _grid) {
        this.grid = _grid;
        this.duration = MyRectangle.getDuration();
        this.rectArr = _rectArr;
        this.transitions = new ArrayList<>();
        sort();
    }

    private void sort() {
        SequentialTransition seqT = new SequentialTransition ();
        int n = this.rectArr.length;
        for (int i = 0; i < n; i++) {
            MyRectangle key = this.rectArr[i];
            int j = i - 1;
            while (j >= 0 && this.rectArr[j].getHeight() > key.getHeight()) {
                this.transitions.add(swapRects(this.rectArr, j, j+1, this.duration));
                j -= 1;
            }
            this.transitions.add(swapRects(this.rectArr, j+1, i, this.duration));
        }
        seqT.getChildren().addAll(this.transitions);
        seqT.play();
    }

}
