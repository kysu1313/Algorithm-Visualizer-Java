package models;

import javafx.scene.layout.AnchorPane;
import models.Vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BinaryTree {

    private Map<Vertex, Number> map;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private int _numNodes;
    private double centerX;
    private double centerY;
    private TreeNode root;
    private AnchorPane graph;
    private final double CHILD_ANGLE = Math.toRadians(20);
    private final int SPACING = 400;

    public BinaryTree() {
        this.map = new HashMap<>();
    }

    public void createTree(double _rootVal, int _numNodes, AnchorPane _pane) throws InterruptedException {
        this._numNodes = _numNodes;
        this.graph = _pane;
        this.x1 = this.graph.getLayoutX();
        this.y1 = this.graph.getLayoutY();
        this.x2 = this.graph.getWidth() + this.x1;
        this.y2 = this.graph.getHeight() + this.y1;

        this.centerX = ((this.x1 + this.x2) / 2) - 130;
        this.centerY = this.y1 + 20;

        this.root = new TreeNode(_rootVal, centerX, centerY, null, null, null);
        this.graph.getChildren().add(this.root);

        this.fillTree(root, this._numNodes, this.graph);

    }

    private void demoFill(TreeNode _parent, int _count, AnchorPane _graph) {
        TreeNode previousRight = null;
        TreeNode previousLeft = null;
        int count = 0;
//        while (count < _count) {
        double rand = Math.random() * _count + 1;
        if (rand > _parent.getValue()) {
            count++;
            // Create right child
            double rightX = this.centerX + 50; // (this.SPACING/_count)
            double rightY = this.centerY + 50;
            TreeNode node = new TreeNode(rand, rightX, rightY, null, null, _parent);
            _parent.setRightChild(node);
            createAndAddArrow(_parent, node);
            demoFill(node, _count-1, _graph);
//                TimeUnit.SECONDS.sleep(1);
            _graph.getChildren().add(node);
        } else {
            count++;
            // Create left child
            double rightX = this.centerX - 50;
            double rightY = this.centerY + 50;
            TreeNode node = new TreeNode(rand, rightX, rightY, null, null, _parent);
            _parent.setLeftChild(node);
            createAndAddArrow(_parent, node);
            demoFill(node, _count-1, _graph);
//                TimeUnit.SECONDS.sleep(1);
            _graph.getChildren().add(node);
        }
//        }
    }

    private void fillTree(TreeNode _parent, int _count, AnchorPane _graph) throws InterruptedException {
        // + for Y goes down
        // + for X goes right



        TreeNode previousRight = null;
        TreeNode previousLeft = null;
        int count = 0;
//        while (count < _count) {
            double rand = Math.random() * _count + 1;
            if (rand > _parent.getValue()) {
                count++;
                // Create right child
                double rightX = this.centerX + 50; // (this.SPACING/_count)
                double rightY = this.centerY + 50;
                TreeNode node = new TreeNode(rand, rightX, rightY, null, null, _parent);
                _parent.setRightChild(node);
                createAndAddArrow(_parent, node);
                fillTree(node, _count-1, _graph);
//                TimeUnit.SECONDS.sleep(1);
                _graph.getChildren().add(node);
            } else {
                count++;
                // Create left child
                double rightX = this.centerX - 50;
                double rightY = this.centerY + 50;
                TreeNode node = new TreeNode(rand, rightX, rightY, null, null, _parent);
                _parent.setLeftChild(node);
                createAndAddArrow(_parent, node);
                fillTree(node, _count-1, _graph);
//                TimeUnit.SECONDS.sleep(1);
                _graph.getChildren().add(node);
            }
//        }

    }

    private Arrow createAndAddArrow(Vertex v1, Vertex v2) {
        Arrow arrow = new Arrow(v1.getLayoutX(), v1.getLayoutY(), v2.getLayoutX(), v2.getLayoutY());
        arrow.x1Property().bind(v1.layoutXProperty());
        arrow.y1Property().bind(v1.layoutYProperty());
        arrow.x2Property().bind(v2.layoutXProperty());
        arrow.y2Property().bind(v2.layoutYProperty());

        v1.edges.add(arrow);
        v2.edges.add(arrow);
        v1.addNeighbor(v2);
        v2.addNeighbor(v1);
        this.graph.getChildren().add(arrow);
        return arrow;
    }

    private TreeNode insertVal(TreeNode _root, double _value) {
        if (_root == null) {
            _root = new TreeNode(_value, this.centerX, this.centerY, null, null, null);
            return _root;
        }

        if (_value < _root.getValue()) {
            root.setLeftChild(insertVal(root.getLeftChild(), _value));
        } else if (_value > root.getValue()) {
            root.setRightChild(insertVal(root.getRightChild(), _value));
        }
        return root;
    }





}
