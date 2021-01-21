package models.blockPath;

import javafx.animation.*;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;

public class Dijkstras {

    private PriorityQueue<MyNode> pq;
    private MyNode[][] nodes;
    private MyNode startNode;
    private MyNode finishNode;
    private List<MyNode> visitedNodes;
    private List<MyNode> unvisitedNodes;
    private Comparator<MyNode> distanceComparator;
    private SequentialTransition stran;
    private int duration = 10;

    public Dijkstras(MyNode[][] _nodes, MyNode _start, MyNode _finish) {
        this.nodes = _nodes;
        this.startNode = _start;
        this.finishNode = _finish;
        this.startNode.setDistance(0.0);
        this.stran = new SequentialTransition();
        this.distanceComparator = new nodeDistanceComparator();
        this.pq = new PriorityQueue<>(distanceComparator);
        this.unvisitedNodes = new LinkedList<>();
        this.visitedNodes = new LinkedList<>();

        begin(this.nodes);
//        this.stran.play();
        getShortestPath(this.stran).play();

    }

    public List<MyNode> begin(MyNode[][] adj) {
//        this.stran.getChildren().add(Grid.createFill(this.startNode, this.duration, Color.GREEN, Color.BLUE));
        this.startNode.setDistance(0);

        // Start by adding all the nodes to the list of unvisited nodes;
        for (int i = 0; i < this.nodes.length; i++) {
            for (int j = 0; j < this.nodes[i].length; j++) {
                this.unvisitedNodes.add(this.nodes[i][j]);
            }
        }

        // Using a priority queue reduces time complexity
        this.pq.addAll(this.unvisitedNodes);

        // While you haven't visited every node, continue;
        while (this.unvisitedNodes.size() > 0) {

            // Refresh the priority queue with the updated unvisited nodes
            this.pq.clear();
            this.pq.addAll(this.unvisitedNodes);

            // Closest node will be the one at the top of the priority queue
            MyNode closestNode = pq.poll();
            this.unvisitedNodes.remove(closestNode);

            // Basically just ignore the node if it's a wall
            if (closestNode.isWall()) {
                continue;
            }

            // If the closest node has max distance, we are stuck and can't proceed;
            if (closestNode.getDistance() == Integer.MAX_VALUE) {
                return visitedNodes;
            }

            // Update visited status
            closestNode.setVisited(true);
            visitedNodes.add(closestNode);

            // Color nodes as we go
            colorNode(closestNode, Color.GREEN);

            // If we're at the end, return;
            if (closestNode == finishNode) {
                return visitedNodes;
            }

            // Update node's neighbors
            updateUnvisitedNeighbors(closestNode);

        }
        return visitedNodes;
    }

    /**
     * Update the distance and parent of a nodes neighbors;
     * @param node
     */
    private void updateUnvisitedNeighbors(MyNode node) {
        PriorityQueue<MyNode> neighbors = getNeighbors(node);
        for (MyNode neighbor : neighbors) {
            colorNode(neighbor, Color.BLUE);
            neighbor.setDistance(node.getDistance()+10);
            neighbor.setParent(node);
        }
    }

    /**
     * Get the neighbors of a node;
     * @param _node
     * @return
     */
    private PriorityQueue<MyNode> getNeighbors(MyNode _node) {
        PriorityQueue<MyNode> neighbors = new PriorityQueue<>(this.distanceComparator);
        List<MyNode> temp = new ArrayList<>();
        if (_node.getRow() > 0) temp.add(this.nodes[_node.getRow()-1][_node.getCol()]);
        if (_node.getRow() < this.nodes.length-1) temp.add(this.nodes[_node.getRow()+1][_node.getCol()]);
        if (_node.getCol() > 0) temp.add(this.nodes[_node.getRow()][_node.getCol()-1]);
        if (_node.getCol() < this.nodes[0].length-1) temp.add(this.nodes[_node.getRow()][_node.getCol()+1]);

        temp.forEach((node) -> {
            if (!node.isVisited()){
                neighbors.add(node);
            }
        });
        return neighbors;
    }

    private void colorNode(MyNode _node, Color _color) {
        if (_node != this.startNode && _node != this.finishNode && !_node.isWall()) {
            this.stran.getChildren().add(Grid.createFill(_node, this.duration, Color.GRAY, _color));
        }
    }

    private List<MyNode> getNodesInShortestOrder(MyNode _finish) {
        List<MyNode> shortest = new ArrayList<>();
        MyNode current = _finish;

        while (current != null) {
            shortest.add(current);
            current = current.getMyParent();
        }
        return shortest;
    }

    /**
     * Custom comparator to check for the distance value.
     */
    private class nodeDistanceComparator implements Comparator<MyNode> {
        @Override
        public int compare(MyNode x, MyNode y) {
            if (x.getDistance() < y.getDistance()) {
                return -1;
            } else if (x.getDistance() > y.getDistance()) {
                return 1;
            }
            return 0;
        }
    }

    private SequentialTransition getShortestPath(SequentialTransition st) {
        getNodesInShortestOrder(this.finishNode).forEach(node -> {
//            colorNode(node, Color.PURPLE);
            st.getChildren().add(Grid.createFill(node, this.duration, Color.GREEN, Color.PURPLE));
        });
        return st;
    }

}


















