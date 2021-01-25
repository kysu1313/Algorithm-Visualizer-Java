package models.blockPath;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

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
    private double SPACING;

    public Grid(int _cols, AnchorPane _pane) {
        this.columns = _cols;
        this.pane = _pane;
        this.SPACING = this.nodeWidth / 5;
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

    public static void randomWalls(MyNode[][] _grid) {
        for (int i = 0; i < _grid.length; i++) {
            for (int j = 0; j < _grid[i].length; j++) {

                if (!_grid[i][j].isStart() && !_grid[i][j].isFinish() && !_grid[i][j].isWall()) {
                    if (j % ((int)(Math.random()*_grid[i].length)+1) == 0) {
                        _grid[i][j].setWall(true);
                    }
                }
            }
        }
    }

    private static List<MyNode> getNodesInShortestOrder(MyNode _finish) {
        List<MyNode> shortest = new ArrayList<>();
        MyNode current = _finish;

        while (current != null) {
            shortest.add(current);
            current = current.getMyParent();
        }
        return shortest;
    }

    public static boolean gridDoesHaveWalls(MyNode[][] _grid) {
        for (int i = 0; i < _grid.length; i++) {
            for (int j = 0; j < _grid[i].length; j++) {
                if (!_grid[i][j].isWall()) {
                    return true;
                }
            }
        }
        return false;
    }

    static SequentialTransition getShortestPath(SequentialTransition st, MyNode _finishNode, int _duration) {
        getNodesInShortestOrder(_finishNode).forEach(node -> {
            st.getChildren().add(Grid.createFill(node, _duration, Color.GREEN, Color.RED));
        });
        return st;
    }

    static void colorNode(MyNode _node, MyNode _startNode, MyNode _finishNode, int _duration, SequentialTransition _stran, Color _color) {
        if (_node != _startNode && _node != _finishNode && !_node.isWall()) {
            _stran.getChildren().add(Grid.createFill(_node, _duration, Color.GRAY, _color));
        }
    }

    static void colorMazeNode(MyNode _node, MyNode _startNode, MyNode _finishNode, int _duration, SequentialTransition _stran, Color _color) {
        if (_node != _startNode && _node != _finishNode && !_node.isWallNode()) {
            _stran.getChildren().add(Grid.createFill(_node, _duration, Color.GRAY, _color));
        }
        else if (_node != _startNode && _node != _finishNode && _node.isWallNode()) {
            _stran.getChildren().add(Grid.createFill(_node, _duration, Color.BLACK, _color));
        }
    }

    static FillTransition createFill(MyNode _node, int _duration, Color _fromColor, Color _toColor) {
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
