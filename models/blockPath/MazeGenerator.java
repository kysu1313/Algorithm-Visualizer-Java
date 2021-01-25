package models.blockPath;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static models.blockPath.Grid.colorMazeNode;
import static models.blockPath.Grid.colorNode;

public class MazeGenerator {

    private MyNode[][] grid;
    private Stack<MyNode> stack;
    private List<MyNode> unvisitedNodes;
    private MyNode current, startNode, finishNode;
    private MyNode tempStart;
    private SequentialTransition stran;
    private SequentialTransition postStran;
    public static int duration = 8;

    public MazeGenerator(MyNode[][] _grid, MyNode _startNode, MyNode _finishNode, boolean _type) {
        this.grid = _grid;
        this.startNode = _startNode;
        this.finishNode = _finishNode;
        this.unvisitedNodes = new LinkedList<>();
        this.unvisitedNodes = getNodes(this.grid);
        this.stack = new Stack<>();
        this.stran = new SequentialTransition();
        this.postStran = new SequentialTransition();
        this.tempStart = this.grid[0][0];

        if (_type) {
            verticalMaze();
        } else {
            makeMaze();
            start();
            cleanNodes();
            this.stran.play();
            this.stran.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    fillWalls();
                }
            });
        }
    }

    /**
     * Creates a grid of every other node.
     * This is used to create "walls" between nodes.
     */
    public void makeMaze() {

            for (int i = 0; i < this.grid.length; i++) {
                for (int j = 0; j < this.grid[i].length; j++) {
                    if (!this.grid[i][j].isStart() && !this.grid[i][j].isFinish()) {
                        if (i % 2 == 0 && j % 2 != 0) {
                            this.grid[i][j].setWallNode(true);
                        }
                        if (i % 2 == 0) {
                            this.grid[i][j].setWallNode(true);
                        }
                        if (j % 2 == 0) {
                            this.grid[i][j].setWallNode(true);
                        }
                    }
                }
            }
        for (int i = 0; i < this.grid.length-1; i++) {
            this.grid[i][this.grid[i].length-1].setWallNode(true);
        }
    }

    /**
     * Clears the color off of the nodes, replacing it with black / gray.
     */
    private void cleanNodes() {
        this.duration = 3;
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j].setVisited(false);
                if (!this.grid[i][j].isWallNode()) {
                    colorMazeNode(this.grid[i][j], this.startNode, this.finishNode, this.duration, this.stran, Color.GRAY);
                } else {
                    colorMazeNode(this.grid[i][j], this.startNode, this.finishNode, this.duration, this.stran, Color.BLACK);
                }
            }
        }
    }

    /**
     * Fills in the walls after coloring animation.
     */
    private void fillWalls() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (this.grid[i][j].isWallNode()) {
                    this.grid[i][j].setWall(true);
                }
            }
        }
    }

    /**
     * Iterative function that generates the maze.
     */
    private void start() {

        MyNode current = this.grid[1][1];
        current.setVisited(true);

        this.stack.push(current);
        while (!this.stack.isEmpty()) {
                current = this.stack.pop();
            MyNode neighbor = this.getNeighbors(current);
            if (neighbor != null) {
                colorNode(neighbor, this.startNode, this.finishNode, duration, this.stran, Color.YELLOW);
                neighbor.setVisited(true);
                this.getWallBetween(current, neighbor);
                neighbor.setVisited(true);
                this.stack.push(neighbor);
            }
            else if (!this.stack.isEmpty()){ //
                current = this.stack.pop();
            }
        }

    }

    /**
     * Get the unvisited neighbors of a node;
     * @param _node
     * @return
     */
    private MyNode getNeighbors(MyNode _node) {
        List<MyNode> temp = new ArrayList<>();
        List<MyNode> neighbors = new ArrayList<>();

        if (_node.getRow() > 1) {
            temp.add(this.grid[_node.getRow()-2][_node.getCol()]);
        }
        if (_node.getRow() < this.grid.length-2) {
            temp.add(this.grid[_node.getRow()+2][_node.getCol()]);
        }
        if (_node.getCol() > 1) {
            temp.add(this.grid[_node.getRow()][_node.getCol()-2]);
        }
        if (_node.getCol() < this.grid[0].length-2) {
            temp.add(this.grid[_node.getRow()][_node.getCol()+2]);
        }

        temp.forEach((node) -> {
            if (!node.isVisited()){

                neighbors.add(node);
                this.stack.push(node);
                colorNode(node, this.startNode, this.finishNode, duration, this.stran, Color.BLUE);
            }
        });

        if (neighbors.size() == 0) {
            return null;
        }
        return neighbors.get((int) (Math.floor(Math.random() * (neighbors.size()))));
    }

    /**
     * Finds the wall between two nodes and removes it.
     * @param n1
     * @param n2
     */
    private void getWallBetween(MyNode n1, MyNode n2) {

        n1.setVisited(true);
        n2.setVisited(true);
        int x1 = n1.getCol();
        int y1 = n1.getRow();

        int x2 = n2.getCol();
        int y2 = n2.getRow();

        int x = x1 - x2;
        int y = y1 - y2;

        if (x == 2) {
            this.grid[y1][x1 - 1].setWallNode(false);
            colorMazeNode(this.grid[y1][x1 - 1], this.startNode, this.finishNode, duration, this.stran, Color.GRAY);
        } else if (x == -2) {
            this.grid[y1][x1 + 1].setWallNode(false);
            colorMazeNode(this.grid[y1][x1 + 1], this.startNode, this.finishNode, duration, this.stran, Color.GRAY);
        } else if (y == 2) {
            this.grid[y1 - 1][x1].setWallNode(false);
            colorMazeNode(this.grid[y1 - 1][x1], this.startNode, this.finishNode, duration, this.stran, Color.GRAY);
        } else if (y == -2) {
            this.grid[y1 + 1][x1].setWallNode(false);
            colorMazeNode(this.grid[y1 + 1][x1], this.startNode, this.finishNode, duration, this.stran, Color.GRAY);
        }
    }

    /**
     * This creates random walls
     * Note: not guaranteed to have a path. Expecially on smaller grids.
     */
    public void verticalMaze() {
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                if (!this.grid[i][j].isStart() && !this.grid[i][j].isFinish()) {
                    if (i % 2 == 0 || j % 2 == 0) {
                        this.grid[i][j].setWall(true);
//                    colorNode(_grid[i][j], this.startNode, this.finishNode, duration, this.stran, Color.BLACK);
                    }
                    double rand1 = Math.random()*this.grid.length;
                    double rand2 = Math.random()*this.grid[0].length;
                    if (j % 2 != 0 || i % 2 != 0 && rand1 > this.grid.length/2 && rand2 > this.grid[0].length/2) {
                        this.grid[i][j].setWall(false);
//                    colorNode(_grid[i][j], this.startNode, this.finishNode, duration, this.stran, Color.BLACK);
                    }
                }
            }
        }
    }

    /**
     * Returns a linked list of all nodes.
     * @param _grid
     * @return
     */
    private List<MyNode> getNodes(MyNode[][] _grid) {
        List<MyNode> temp = new LinkedList<>();
        for (int i = 0; i < _grid.length; i++) {
            for (int j = 0; j < _grid[i].length; j++) {
                temp.add(_grid[i][j]);
            }
        }
        return temp;
    }


}
