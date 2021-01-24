package models.blockPath;

import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
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
    private int duration = 100;

    public MazeGenerator(MyNode[][] _grid, MyNode _startNode, MyNode _finishNode) {
        this.grid = _grid;
        this.startNode = _startNode;
        this.finishNode = _finishNode;
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
        unvisitedNodes.remove(current);

        while (!this.unvisitedNodes.isEmpty()) {
            MyNode neighbor = this.getNeighbors(current);
            if (neighbor != null) {
                neighbor.setVisited(true);
                this.stack.push(current);
                this.getWallBetween(current, neighbor);
//                clearPath(neighbor);
//                MyNode wall = getWallBetween(current, neighbor);
//                assert wall != null;
//                wall.setWall(true);
//                wall.setVisited(true);
//                wall.setFill(Color.BLACK);

                this.unvisitedNodes.remove(neighbor);
//                colorNode(neighbor, this.startNode, this.finishNode, duration, this.stran, Color.BLUE);
                current = neighbor;
                current.setWall(false);
                current.setVisited(true);
//                current.setFill(Color.BLACK);

                unvisitedNodes.remove(current);
            } else if (!this.stack.isEmpty()) {
                current = this.stack.pop();
            } else {
//                current = this.unvisitedNodes.remove((int)(Math.random()*this.unvisitedNodes.size()));
                current.setWall(false);
                current.setVisited(false);
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
            }
        });

        if (neighbors.size() == 0) {
            return null;
        }
        return neighbors.get((int) (Math.floor(Math.random() * (neighbors.size()))));
    }

    private void clearPath(MyNode node) {
        assert node != null;
        node.setVisited(true);
        if (node.isNorth()) this.grid[node.getRow()][node.getCol()+1].setWall(false);
        else if (node.isSouth()) this.grid[node.getRow()][node.getCol()-1].setWall(false);
        else if (node.isEast()) this.grid[node.getRow()-1][node.getCol()].setWall(false);
        else if (node.isWest()) this.grid[node.getRow()+1][node.getCol()].setWall(false);
        clearNodeDirection(node);
    }

    private void clearNodeDirection(MyNode node) {
        if (node.isNorth()) node.setNorth(false);
        if (node.isSouth()) node.setSouth(false);
        if (node.isEast()) node.setEast(false);
        if (node.isWest()) node.setWest(false);
    }

    private void getWallBetween(MyNode n1, MyNode n2) {

        int x1 = n1.getCol();
        int y1 = n1.getRow();

        int x2 = n2.getCol();
        int y2 = n2.getRow();

        int x = x1 - x2;
        int y = y1 - y2;

        if (x == 2) {
            this.grid[x1-1][y1].setWall(false);
        } else if (x == -2) {
            this.grid[x1+1][y1].setWall(false);
        } else if (y == 2) {
            this.grid[x1][y1-1].setWall(false);
        } else if (y == -2) {
            this.grid[x1][y1-1].setWall(false);
        }
//        if (x != 0 && y != 0) {
//            return null;
//        }
//        x = n2.getCol() + (x/2) - 1;
//        y = n2.getRow() + (y/1) - 1;
//        if (this.grid.length-1 > x && this.grid[0].length-1 > y && x >= 0 && y >= 0) {
//            return this.grid[x][y];
//        } else {
//            return n1;
//        }
    }

//    public void generate() {
//        MyNode current = this.unvisitedNodes.get((int)(Math.random()*this.unvisitedNodes.size()));
//        current.setWall(true);
//        current.setVisited(true);
//        current.setFill(Color.BLACK);
////        this.tempStart = null;
////        this.tempFinish = null;
//
//        while (!this.unvisitedNodes.isEmpty()) {
//            if (unvisitedNodes.size() == 1) {
////                tempFinish = unvisitedNodes.get(0);
//            }
//            MyNode neighbor = this.getNeighbor(current);
////            MyNode secondNeighbor = this.getNeighbor(neighbor);
//
//            if (neighbor != null) {
//                this.stack.push(current);
//                MyNode wall = getWallBetween(current, neighbor);
//                assert wall != null;
//                wall.setWall(true);
//                wall.setVisited(true);
//                wall.setFill(Color.BLACK);
//
//                this.unvisitedNodes.remove(wall);
//                colorNode(neighbor, this.startNode, this.finishNode, duration, this.stran, Color.BLUE);
//                current = neighbor;
//                current.setWall(false);
//                current.setVisited(true);
//                current.setFill(Color.BLACK);
//
//                unvisitedNodes.remove(current);
//            } else if (!this.stack.isEmpty()) {
//                current = this.stack.pop();
//            } else {
//                current = this.unvisitedNodes.remove((int)(Math.random()*this.unvisitedNodes.size()));
//                current.setWall(false);
//                current.setVisited(false);
//            }
//        }
//        this.stran.play();
//
//    }

    private boolean chooseOrientation(int width, int height) {
        if (width < height) return true;
        if (height < width) return false;
        return (int) (Math.ceil(Math.random() * 2)) == 1;
    }

    private MyNode[][] divideArr(MyNode[][] grid, int horizVert) {

//        int rand1 = (int)(Math.ceil(Math.random() * (grid[0].length-3))+1);
//        int rand2 = (int)(Math.ceil(Math.random() * (grid.length-3))+1);
        int pathPoint = (int)(Math.ceil(Math.random() * 2));

        Random rand = new Random();
        // row
        int rand1 = rand.nextInt((grid[0].length-3)/2) *2;
        // col
        int rand2 = rand.nextInt((grid.length-3)/2) *2;
        int tempRand = 0;

        // Vertical Split
        if (horizVert == 1) {
            int colLen = 1;
            for (int i = 0; i < grid[0].length; i++) {
                if (grid[rand2][i].isWall()) {
                    tempRand = Math.max(rand.nextInt((colLen + 1) / 2) * 2, 0);
                    grid[tempRand][rand2].setWall(false);
                    return grid;
                }
                grid[rand2][i].setWall(true);
                colLen++;
            }
            tempRand = Math.max(rand.nextInt((colLen+1)/2) *2, 0);
            grid[0][rand2].setWall(false);
            // Horizontal Split
        } else if (horizVert == 2) {
            int rowLen = 1;
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][rand1].isWall()) {
                    tempRand = Math.max(rand.nextInt((rowLen+1)/2) *2, 0); // tempRand = ((int)(Math.ceil(Math.random() * (grid.length-2))));
                    grid[tempRand][rand1].setWall(false);
                    return grid;
                }
                grid[i][rand1].setWall(true);
                rowLen++;
            }
            tempRand = Math.max(rand.nextInt((rowLen+1)/2) *2, 0);
            grid[0][rand1].setWall(false);  // ((int)(Math.ceil(Math.random() * (grid.length-1))))
        }
        return grid;
    }



//    private List<List<MyNode>> divide(List<List<MyNode>> grid, int horizVert) {
//
//        int rand1 = grid.get(0).size() / new Random(5).nextInt();
//        int rand2 = grid.size() / new Random(5).nextInt();
//        // Vertical Split
//        if (horizVert == 1) {
//            for (int i = 0; i < grid.get(0).size(); i++) {
//                grid.get(rand1).get(i).setWall(true);
//            }
//            grid.get(rand1).get((int)(Math.ceil(Math.random() * grid.get(rand1).size()))).setWall(false);
//        // Horizontal Split
//        } else if (horizVert == 2) {
//            for (int i = 0; i < grid.size(); i++) {
//                grid.get(i).get(rand2).setWall(true);
//            }
//            grid.get(rand2).get((int)(Math.ceil(Math.random() * grid.get(rand2).size()))).setWall(false);
//        }
//        return grid;
//    }

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



    private MyNode getRandomNeighbor(MyNode current) {
        return this.unvisitedNodes.get((int)(Math.random()*this.unvisitedNodes.size()));
    }

    private MyNode getNeighbor(MyNode current) {
        List<MyNode> temp = new ArrayList<>();
        List<MyNode> toChooseFrom = new ArrayList<>();

        if (current.getRow() > 1){
            temp.add(this.grid[current.getRow()-2][current.getCol()]); // south
            this.grid[current.getRow()-2][current.getCol()].setSouth(true);
//            this.grid[current.getRow()-2][current.getCol()].setWall(true);
        }
        if (current.getRow() < this.grid.length-2){
            temp.add(this.grid[current.getRow()+2][current.getCol()]); // north
            this.grid[current.getRow()+2][current.getCol()].setNorth(true);
//            this.grid[current.getRow()+2][current.getCol()].setWall(true);
        }
        if (current.getCol() > 1){
            temp.add(this.grid[current.getRow()][current.getCol()-2]); // east
            this.grid[current.getRow()][current.getCol()-2].setEast(true);
//            this.grid[current.getRow()][current.getCol()-2].setWall(true);
        }
        if (current.getCol() < this.grid[0].length-2){
            temp.add(this.grid[current.getRow()][current.getCol()+2]); // west
            this.grid[current.getRow()][current.getCol()+2].setWest(true);
//            this.grid[current.getRow()][current.getCol()+2].setWall(true);
        }

        for (MyNode node : temp) {
            if (!node.isVisited()) {
                toChooseFrom.add(node);
            }
            node.setVisited(true);
        }

        if (toChooseFrom.size() == 0) {
            return null;
        }
        System.out.println("Current row : col : " + current.getRow() + ",  " + current.getCol());
        MyNode next = toChooseFrom.get(new Random().nextInt(toChooseFrom.size()));
        if (next.isSouth()){
            this.grid[current.getRow()-1][current.getCol()].setWall(false);
            next.setSouth(false);
            System.out.println((current.getRow()-1) + ",  " + current.getCol());
        }
        if (next.isNorth()){
            this.grid[current.getRow()+1][current.getCol()].setWall(false);
            next.setNorth(false);
            System.out.println((current.getRow()+1) + ",  " + current.getCol());
        }
        if (next.isEast()){
            this.grid[current.getRow()][current.getCol()-1].setWall(false);
            next.setEast(false);
            System.out.println(current.getRow() + ",  " + (current.getCol()-1));
        }
        if (next.isWest()){
            this.grid[current.getRow()][current.getCol()+1].setWall(false);
            next.setWest(false);
            System.out.println(current.getRow() + ",  " + (current.getCol()+1));
        }
        return (toChooseFrom.isEmpty()) ? null : next; // (int)(Math.ceil(Math.random()*(toChooseFrom.size()-1)))
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
//                if (!_grid[i][j].isStart() && !_grid[i][j].isFinish()) {
//                    if (i % 2 == 0 || j % 2 == 0) {
//                        _grid[i][j].setWall(true);
//                        colorNode(_grid[i][j], this.startNode, this.finishNode, duration, this.stran, Color.BLACK);
//                    }
//                    double rand1 = Math.random()*_grid.length;
//                    double rand2 = Math.random()*_grid[0].length;
//                    if (j % 2 != 0 || i % 2 != 0 && rand1 > _grid.length/2 && rand2 > _grid[0].length/2) { //  && rand1 > _grid.length/2  rand1 > _grid.length/2 && rand2 > _grid[0].length/2
//                        _grid[i][j].setWall(false);
//                        colorNode(_grid[i][j], this.startNode, this.finishNode, duration, this.stran, Color.BLACK);
//                    }
//                }
            }
        }
//        this.stran.play();
        return temp;
    }



//    private void divide(MyNode[][] grid, int x, int y, int width, int height, boolean orientation, int count) {
//        count++;
//        if (count >= grid.length * grid[0].length) return;
//        // Orientation = vertical if true, horizontal if false
//        boolean horizontal = orientation;
//
//        // Where to draw the line
//        int wx = x + (horizontal ? 0 : (int)(Math.ceil(Math.random() * (width-2))));
//        int wy = y + (horizontal ? (int)(Math.ceil(Math.random() * (width-2))) : 0);
//
//        // Where to make the opening
//        int px = wx + (horizontal ? (int)(Math.ceil(Math.random() * (width))) : 0);
//        int py = wy + (horizontal ? 0 : (int)(Math.ceil(Math.random() * (width))));
//
//        // Which direction to draw line
//        int dx = horizontal ? 1 : 0;
//        int dy = horizontal ? 0 : 1;
//
//        // How long is the wall
//        int length = horizontal ? width : height;
//
//        // Perpendicular direction (south = 1, east = 2)
//        int dir = horizontal ? 1 : 2;
//
//        for (int i = 0; i < length-2; i++) {
//            if (wx != px || wy != py) {
//                if (dir == 1) {
//                    grid[wy][wx].setSouth(true);
//                    grid[wy][wx+1].setWall(true);
//                } else {
//                    grid[wy][wx].setEast(true);
//                    grid[wy+1][wx].setWall(true);
//                }
//                wx += dx;
//                wy += dy;
//            }
//        }
//
//        int nx = x;
//        int ny = y;
//        int w = 0;
//        int h = 0;
//
//        if (horizontal) {
//            w = width;
//            h = wy - y + 1;
//        } else {
//            w = wx - x + 1;
//            h = height;
//        }
//        divide(grid, nx, ny, w, h, chooseOrientation(w, h), count);
//
//
//        if (horizontal) {
//            nx = x;
//            ny = wy+1;
//            w = width;
//            h = y + height - wy - 1;
//        } else {
//            nx = wx + 1;
//            ny = y;
//            w = x + width - wx - 1;
//            h = height;
//        }
//        divide(grid, nx, ny, w, h, chooseOrientation(w, h), count);
//
//    }

}
