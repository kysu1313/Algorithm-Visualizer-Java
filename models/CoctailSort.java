package models;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static models.SortRectangles.colorRect;

public class CoctailSort {

    private MyRectangle[] rectArr;
    private int duration;
    private AnchorPane grid;
    private ArrayList<Transition> transitions;
    private ArrayList<Transition> fadeTransitions;

    public CoctailSort(MyRectangle[] _rectArr, AnchorPane _grid) {
        this.rectArr = _rectArr;
        this.duration = MyRectangle.getDuration();
        this.transitions = new ArrayList<>();
        this.grid = _grid;
        sort();
    }

    private void sort() {
        SequentialTransition seqT = new SequentialTransition ();
        boolean swapped = true;
        int start = 0;
        int end = this.rectArr.length;

        while (swapped) {
            swapped = false;
            for (int i = start; i < end-1; ++i) {
                this.transitions.add(colorRect(this.rectArr[i], Color.RED, this.duration));
                this.transitions.add(colorRect(this.rectArr[i+1], Color.RED, this.duration));
                if (this.rectArr[i].getHeight() > this.rectArr[i+1].getHeight()) {
                    this.transitions.add(SortRectangles.swapRects(this.rectArr, i, i+1, this.duration));
                    swapped = true;
                }
                this.transitions.add(colorRect(this.rectArr[i], Color.BLUE, this.duration));
                this.transitions.add(colorRect(this.rectArr[i+1], Color.BLUE, this.duration));
            }
            if (!swapped) {
                break;
            }
            swapped = false;
            end = end-1;

            for (int i = end-1; i >= start; i--) {
                this.transitions.add(colorRect(this.rectArr[i], Color.RED, this.duration));
                this.transitions.add(colorRect(this.rectArr[i+1], Color.RED, this.duration));
                if (this.rectArr[i].getHeight() > this.rectArr[i+1].getHeight()) {
                    this.transitions.add(SortRectangles.swapRects(this.rectArr,i,  i+1, this.duration));
                    swapped = true;
                }
                this.transitions.add(colorRect(this.rectArr[i], Color.BLUE, this.duration));
                this.transitions.add(colorRect(this.rectArr[i+1], Color.BLUE, this.duration));
            }
            start += 1;
        }
        seqT.getChildren().addAll(this.transitions);
        seqT.play();
    }
}
