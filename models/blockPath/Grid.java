package models.blockPath;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Grid {

    private int rows;
    private int columns;
    private int numCols;
    private AnchorPane pane;
    private MyNode[][] grid;
    private double nodeWidth;
    private MyNode[] flatNodes;
    private MyNode startNode;
    private MyNode finishNode;
    private final static double SPACING = 5;

    public Grid(int _cols, AnchorPane _pane) {
        this.columns = _cols;
        this.pane = _pane;
        this.nodeWidth = (this.pane.getWidth() / (_cols+SPACING));  //  - 4
        this.rows = (int)Math.floor(this.pane.getHeight()/(nodeWidth+SPACING));
        this.rows--;
        this.columns--;
        this.grid = new MyNode[this.rows][this.columns];

    }

    public MyNode[][] makeGrid() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                double xLoc = (j*nodeWidth) + (j*SPACING);
                double yLoc = (i*nodeWidth) + (i*SPACING);
                this.grid[i][j] = new MyNode(xLoc, yLoc, nodeWidth, nodeWidth, i, j);
            }
        }
        this.flatNodes = getFlattenedNodes();
        return this.grid;
    }

    public MyNode[][] getNodes() {
        return this.grid;
    }

    public static FillTransition createFill(MyNode _node, int _duration, Color _fromColor, Color _toColor) {
        FillTransition tran = new FillTransition(Duration.millis(_duration), _node);
//        tran.setFromValue(_fromColor);
        tran.setToValue(_toColor);
        return tran;
    }

    /**
     * Determine what node the mouse is dragging over.
     * Used for making walls.
     * @param flatNodes
     * @param _x
     * @param _y
     * @return
     */
    public static MyNode getNode(MyNode[] flatNodes, double _x, double _y) {
        for (MyNode node : flatNodes) {
            if (node.getX() <= _x && node.getX()+node.getWidth() >= _x
                    && node.getY() <= _y && node.getY()+node.getWidth() >= _y) {
                return node;
            }
        }
        return new MyNode();
    }

    public MyNode[] getFlattenedNodes() {
        MyNode[] nds = new MyNode[this.rows * this.columns];
        int index = 0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                nds[index++] = this.grid[i][j];
            }
        }
        return nds;
    }

}
