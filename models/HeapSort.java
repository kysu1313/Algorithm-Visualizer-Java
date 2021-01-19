package models;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static models.SortRectangles.colorRect;
import static models.SortRectangles.swapRects;

public class HeapSort {

    private AnchorPane grid;
    private int numBars;
    private MyRectangle[] rectArr;
    private ArrayList<Transition> transitions;
    private ArrayList<Transition> fadeTransitions;
    private int duration;

    public HeapSort(MyRectangle[] _rectArr, AnchorPane _grid) {
        this.transitions = new ArrayList<>();
        this.grid = _grid;
//        this.numBars = _numBars;
        this.rectArr = _rectArr;
        this.duration = MyRectangle.getDuration();
    }

    public void sort() {
        SequentialTransition seqT = new SequentialTransition ();
        int n = this.rectArr.length;
        this.transitions.add(colorRect(findBranch(this.rectArr, this.rectArr.length), Color.RED, this.duration));

        for (int i = n/2-1; i >= 0; i--) {
            heapify(this.rectArr, n, i);
        }
        this.transitions.add(colorRect(findBranch(this.rectArr, this.rectArr.length), Color.BLUE, this.duration));
        for (int i = this.rectArr.length-1; i >= 0; i--) {

            this.transitions.add(colorRect(this.rectArr, Color.RED, 0));
            this.transitions.add(swapRects(this.rectArr, 0, i, this.duration));
            this.transitions.add(colorRect(this.rectArr, Color.RED, this.duration, i));
            this.transitions.add(colorRect(findBranch(this.rectArr, i), Color.RED, this.duration));
            heapify(this.rectArr, i, 0);
            this.transitions.add(colorRect(findBranch(this.rectArr, i), Color.BLUE, this.duration));
        }
        seqT.getChildren().addAll(this.transitions);
        seqT.play();
    }

    /**
     * Heapify the subtree at node i.
     * @param _arr
     * @param _heapSize
     * @param _root
     */
    private void heapify(MyRectangle[] _arr, int _heapSize, int _root) {
        int largest = _root;
        int l = 2 * _root + 1;
        int r = 2 * _root + 2;

        // Checks if the left subtree is larger than the root
        if (r < _heapSize && _arr[l].getHeight() > _arr[largest].getHeight()) {
            largest = l;
        }

        // Checks if the right subtree is larger than the root
        if (r < _heapSize && _arr[r].getHeight() > _arr[largest].getHeight()) {
            largest = r;
        }

        // If the root is not the largest value
        if (largest != _root) {
            this.transitions.add(swapRects(this.rectArr, largest, _root, this.duration));
            heapify(_arr, _heapSize, largest);
        }
    }

    private ArrayList<MyRectangle> findBranch(MyRectangle[] _arr, int _a) {
        ArrayList<MyRectangle> arrLst = new ArrayList<>();
        for (int i = 0; i < _a; i++) {
            arrLst.add(_arr[i]);
        }
        return arrLst;
    }

}
