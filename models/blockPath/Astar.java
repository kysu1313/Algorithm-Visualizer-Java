package models.blockPath;

import javafx.animation.SequentialTransition;
import javafx.scene.paint.Color;

import java.util.*;

import static models.blockPath.Grid.colorNode;
import static models.blockPath.Grid.getShortestPath;

public class Astar {

    private MyNode[][] nodes;
    private MyNode startNode;
    private MyNode finishNode;
    private List<MyNode> visitedNodes;
    private List<MyNode> visitedNodesInOrder;
    private Deque<MyNode> tempVisitedNodes;
    private List<MyNode> openSet;
    private List<MyNode> closedSet;
    private Comparator<MyNode> fScoreComparator;
    private PriorityQueue<MyNode> pq;
    private SequentialTransition stran;
    private boolean horizontals;
    public static int duration = 1;

    public Astar(MyNode[][] _nodes, MyNode _startNode, MyNode _finishNode, boolean _horizontals) {
        this.horizontals = _horizontals;
        this.nodes = _nodes;
        this.startNode = _startNode;
        this.finishNode = _finishNode;
        this.startNode.setDistance(0);
        this.startNode.sethCost(0);
        this.startNode.setgCost(0);
        this.startNode.setfCost(0);
        this.visitedNodes = new LinkedList<>();
        this.visitedNodesInOrder = new LinkedList<>();
        this.openSet = new LinkedList<>();
        this.closedSet = new LinkedList<>();
        this.tempVisitedNodes = new LinkedList<>();
        this.fScoreComparator = new fScoreComparator();
        this.pq = new PriorityQueue<>(this.fScoreComparator);
        this.stran = new SequentialTransition();

        begin();
        getShortestPath(this.stran, this.finishNode, duration).play();
    }

    private void begin() {
        this.openSet.add(startNode);
        MyNode current = null;

        while (openSet.size() > 0) {
            pq.clear();
            pq.addAll(openSet);
            current = pq.remove();

            if (current.getParent() == null && current != this.startNode) {
                return;
            }

            if (current == this.finishNode) {
                return;
            }

            this.openSet.remove(current);
            this.closedSet.add(current);
            colorNode(current, this.startNode, this.finishNode, duration, this.stran, Color.BLUE);
            List<MyNode> neighbors = getNeighbors(current);
            for (MyNode neighbor : neighbors) {
                this.visitedNodes.add(neighbor);
                this.tempVisitedNodes.add(neighbor);

                if (!neighbor.isWall() && !closedSet.contains(neighbor)) {
                    double tempG = current.getgCost() + manhattanDistance(neighbor, this.finishNode);

                    if (this.openSet.contains(neighbor)) {
                        if (tempG < neighbor.getgCost()) {
                            neighbor.setgCost(tempG);
                        }
                    } else {
                        neighbor.setgCost(tempG);
                        neighbor.setVisited(true);
                        colorNode(neighbor, this.startNode, this.finishNode, duration, this.stran, Color.YELLOW);
                        this.openSet.add(neighbor);
                    }

                    neighbor.setParent(current);
                    neighbor.sethCost(manhattanDistance(neighbor, this.finishNode));
                    neighbor.setfCost(neighbor.getgCost() + neighbor.gethCost());
                }

            }
        }
    }

    private double manhattanDistance(MyNode _neighbor, MyNode _finishNode) {
        return Math.abs(_neighbor.getCol() - _finishNode.getCol()) + Math.abs(_neighbor.getRow() - _finishNode.getRow());
    }

    /**
     * Get the neighbors of a node;
     * @param _node
     * @return
     */
    private List<MyNode> getNeighbors(MyNode _node) {
        List<MyNode> neighbors = new ArrayList<>();
        List<MyNode> temp = new ArrayList<>();
        if (_node.getRow() > 0) temp.add(this.nodes[_node.getRow()-1][_node.getCol()]);
        if (_node.getRow() < this.nodes.length-1) temp.add(this.nodes[_node.getRow()+1][_node.getCol()]);
        if (_node.getCol() > 0) temp.add(this.nodes[_node.getRow()][_node.getCol()-1]);
        if (_node.getCol() < this.nodes[0].length-1) temp.add(this.nodes[_node.getRow()][_node.getCol()+1]);

        if (this.horizontals) {
            if (_node.getRow() > 0 && _node.getCol() > 0) temp.add(this.nodes[_node.getRow()-1][_node.getCol()-1]);
            if (_node.getRow() > 0 && _node.getCol() < this.nodes[0].length-1)  temp.add(this.nodes[_node.getRow()-1][_node.getCol()+1]);
            if (_node.getRow() < this.nodes.length-1 && _node.getCol() > 0)  temp.add(this.nodes[_node.getRow()+1][_node.getCol()-1]);
            if (_node.getRow() < this.nodes.length-1 && _node.getCol() < this.nodes[0].length-1)  temp.add(this.nodes[_node.getRow()+1][_node.getCol()+1]);
        }

        temp.forEach((node) -> {
            if (node.isFinish()){
                System.out.println("finish node found :)");
            }
            if (!node.isVisited()){
                neighbors.add(node);
            }
        });
        return neighbors;
    }

    /**
     * Custom comparator to check for the distance value.
     */
    private class fScoreComparator implements Comparator<MyNode> {
        @Override
        public int compare(MyNode x, MyNode y) {
            if (x.getfCost() < y.getfCost()) {
                return -1;
            } else if (x.getfCost() > y.getfCost()) {
                return 1;
            }
            return 0;
        }
    }

}
