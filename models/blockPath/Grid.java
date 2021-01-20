package models.blockPath;

import javafx.scene.layout.AnchorPane;

public class Grid {

    private int rows;
    private int columns;
    private int numCols;
    private AnchorPane pane;
    private MyNode[][] grid;
    private double nodeWidth;
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
                this.grid[i][j] = new MyNode(xLoc, yLoc, nodeWidth, nodeWidth);
            }
        }
        return this.grid;
    }

}
