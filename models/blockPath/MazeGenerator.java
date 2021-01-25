package models.blockPath;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static models.blockPath.Grid.colorNode;

public class MazeGenerator {

    private MyNode[][] grid;
    private Stack<MyNode> stack;
    private List<MyNode> unvisitedNodes;
    private MyNode current, startNode, finishNode;
    private MyNode tempStart;
    private SequentialTransition stran;
    private AnchorPane graph;
    private int duration = 100;

    public MazeGenerator(MyNode[][] _grid, MyNode _startNode, MyNode _finishNode, AnchorPane grid) {
        this.grid = _grid;
        this.startNode = _startNode;
        this.finishNode = _finishNode;
        this.graph = grid;
        this.unvisitedNodes = new LinkedList<>();
        this.unvisitedNodes = getNodes(this.grid);
        this.stack = new Stack<>();
        this.stran = new SequentialTransition();
        this.tempStart = this.grid[0][0];

        makeMaze();
        start();
//        DFSGenerator();
//        generate();
    }

//    public void make(int x, int y) {
//        if (step(x, y)) {
//
//        }
//    }

    public void makeMaze() {

            for (int i = 0; i < this.grid.length; i++) {
                for (int j = 0; j < this.grid[i].length; j++) {
                    if (!this.grid[i][j].isStart() && !this.grid[i][j].isFinish()) {
                        if (i % 2 == 0 && j % 2 != 0) {
                            this.grid[i][j].setWall(true);
                        }
                        if (i % 2 == 0) {
                            this.grid[i][j].setWall(true);
                        }
                        if (j % 2 == 0) {
                            this.grid[i][j].setWall(true);
                        }
                    }
                }
            }
        for (int i = 0; i < this.grid.length-1; i++) {
            this.grid[i][this.grid[i].length-1].setWall(true);
        }
        System.out.println("rows: " + this.grid.length); // rows
        System.out.println("cols: " + this.grid[0].length); // columns
        System.out.println();
//        recur(this.grid[1][1]);
    }

//    private void recur(MyNode node) {
//        node.setVisited(true);
//        MyNode neighbor = this.getNeighbors(node);
//        while (neighbor != null) {
//            updateGraph();
//            this.getWallBetween(node, neighbor);
//            recur(neighbor);
//        }
//    }

    private void updateGraph() {
        this.graph.getChildren().clear();
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.graph.getChildren().addAll(this.grid[i][j]);
            }
        }
    }

    private void start() {

        List<MyNode> unvisitedNodes = new ArrayList<>();
        for (int i = 0; i < this.grid.length-1; i++) {
            for (int j = 0; j < this.grid[i].length-1; j++) {
                if (!this.grid[i][j].isWall()) {
                    unvisitedNodes.add(this.grid[i][j]);
                }
            }
        }

        MyNode current = this.grid[1][1];
        current.setVisited(true);
//        unvisitedNodes.remove(current);

        this.stack.push(current);
        while (!this.stack.isEmpty()) {  // !this.unvisitedNodes.isEmpty()
//            if (!this.stack.isEmpty()) {
                current = this.stack.pop();
//            } else {
//                current = this.unvisitedNodes.remove((int)(Math.random()*this.unvisitedNodes.size()));
//            }
            MyNode neighbor = this.getNeighbors(current);
//            Platform.runLater(this::updateGraph);
            if (neighbor != null) {
                neighbor.setVisited(true);
//                this.stack.push(current);
                this.getWallBetween(current, neighbor);
                neighbor.setVisited(true);
                this.stack.push(neighbor);
//                clearPath(neighbor);
//                MyNode wall = getWallBetween(current, neighbor);
//                assert wall != null;
//                wall.setWall(true);
//                wall.setVisited(true);
//                wall.setFill(Color.BLACK);

//                this.unvisitedNodes.remove(neighbor);
//                colorNode(neighbor, this.startNode, this.finishNode, duration, this.stran, Color.BLUE);
//                current = neighbor;
//                current.setWall(false);
//                current.setVisited(true);
//                current.setFill(Color.BLACK);

//                unvisitedNodes.remove(current);
            }
            else if (!this.stack.isEmpty()){ //
                current = this.stack.pop();
            }
        }

    }

    /**
     * Get the neighbors of a node;
     * @param _node
     * @return
     */
    private MyNode getNeighbors(MyNode _node) {
        List<MyNode> temp = new ArrayList<>();
        List<MyNode> neighbors = new ArrayList<>();

        if (_node.getRow() > 1) {
            this.grid[_node.getRow()-2][_node.getCol()].setEast(true);
            temp.add(this.grid[_node.getRow()-2][_node.getCol()]);
        }
        if (_node.getRow() < this.grid.length-2) {
            this.grid[_node.getRow()+2][_node.getCol()].setWest(true);
            temp.add(this.grid[_node.getRow()+2][_node.getCol()]);
        }
        if (_node.getCol() > 1) {
            this.grid[_node.getRow()][_node.getCol()-2].setSouth(true);
            temp.add(this.grid[_node.getRow()][_node.getCol()-2]);
        }
        if (_node.getCol() < this.grid[0].length-2) {
            this.grid[_node.getRow()][_node.getCol()+2].setNorth(true);
            temp.add(this.grid[_node.getRow()][_node.getCol()+2]);
        }

        temp.forEach((node) -> {
            if (!node.isVisited()){

                neighbors.add(node);
                this.stack.push(node);
            }
        });

        if (neighbors.size() == 0) {
            return null;
        }
        return neighbors.get((int) (Math.floor(Math.random() * (neighbors.size()))));
    }

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
            this.grid[y1][x1 - 1].setWall(false);
//            this.grid[y1][x1 - 1].setVisited(true);
        } else if (x == -2) {
            this.grid[y1][x1 + 1].setWall(false);
//            this.grid[y1][x1 + 1].setVisited(true);
        } else if (y == 2) {
            this.grid[y1 - 1][x1].setWall(false);
//            this.grid[y1 - 1][x1].setVisited(true);
        } else if (y == -2) {
            this.grid[y1 + 1][x1].setWall(false);
//            this.grid[y1 + 1][x1].setVisited(true);
        }
    }

    private List<List<MyNode>> getlistNodes(MyNode[][] grid) {
        List<List<MyNode>> list = new ArrayList<>();
        for (MyNode[] inner : grid) {
            List<MyNode> tmp = new ArrayList<>();
            for (MyNode node : inner) {
                tmp.add(node);
            }
            list.add(tmp);
        }
        return list;
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
