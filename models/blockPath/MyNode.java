package models.blockPath;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MyNode extends Rectangle {

    private double x;
    private double y;
    private int row;
    private int col;
    private double width;
    private double height;
    private boolean isWall;
    private boolean isVisited;
    private boolean isStart;
    private boolean isFinish;
    private double fCost;
    private double gCost;
    private double hCost;
    private double distance;
    private boolean east;
    private boolean west;
    private boolean north;
    private boolean south;
    private boolean isWallNode;
    private MyNode parent;
    private Color color;
    private String backgroundColor;

    // top 0, right 1, bottom 2, left 3
    private boolean[] walls = {true, true, true, true};

    public MyNode(double _x, double _y, double _width, double _height, int _row, int _col) {
        super(_x, _y, _width, _height);
        this.x = _x;
        this.y = _y;
        this.row = _row;
        this.col = _col;
        this.width = _width;
        this.height = this.width; // squares
        this.isWall = false;
        this.isVisited = false;
        this.isStart = false;
        this.isFinish = false;
        this.east = false;
        this.west = false;
        this.north = false;
        this.south = false;
        this.parent = null;
        this.fCost = 0;
        this.gCost = 0;
        this.hCost = 0;
        this.distance = Integer.MAX_VALUE;
        setNodeBackgroundColor("darkgray", "black");
        this.color = Color.WHITE;
    }

    public MyNode() {
        this.isWall = false;
        this.isVisited = false;
        this.isStart = false;
        this.isFinish = false;
        this.parent = null;
        this.fCost = 0;
        this.gCost = 0;
        this.hCost = Integer.MAX_VALUE;
        this.distance = Integer.MAX_VALUE;
        setNodeBackgroundColor("darkgray", "black");
        this.color = Color.WHITE;
    }

    public void setNodeBackgroundColor(String _color, String _borderColor) {
        this.backgroundColor = _color;
        this.setStyle("-fx-fill: " + _color + "; -fx-stroke: black; -fx-stroke-width: 1;");
    }

    public void removeWalls(MyNode next) {
        int x = this.getCol() - next.getCol();
        // top 0, right 1, bottom 2, left 3

        if(x == 1) {
            walls[3] = false;
            next.walls[1] = false;
        } else if (x == -1) {
            walls[1] = false;
            next.walls[3] = false;
        }

        int y = this.getRow() - next.getRow();

        if(y == 1) {
            walls[0] = false;
            next.walls[2] = false;
        } else if (y == -1) {
            walls[2] = false;
            next.walls[0] = false;
        }
    }

    public boolean isWallNode() {
        return isWallNode;
    }

    public void setWallNode(boolean path) {
        isWallNode = path;
    }

    public boolean[] getWalls() {
        return walls;
    }

    public void setWalls(boolean[] walls) {
        this.walls = walls;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        if (finish) {
            this.setNodeBackgroundColor("red", "black");
        } else {
            this.setNodeBackgroundColor("darkgray", "black");
        }
        isFinish = finish;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        if (start) {
            this.setNodeBackgroundColor("green", "black");
        } else {
            this.setNodeBackgroundColor("darkgray", "black");
        }
        isStart = start;
    }

    public MyNode getMyParent() {
        return parent;
    }

    public void setParent(MyNode parent) {
        this.parent = parent;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        if (wall) {
            setNodeBackgroundColor("black", "black");
        } else {
            setNodeBackgroundColor("darkgray", "black");
        }
        isWall = wall;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public double getfCost() {
        return fCost;
    }

    public void setfCost(double fCost) {
        this.fCost = fCost;
    }

    public double getgCost() {
        return gCost;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public double gethCost() {
        return hCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
