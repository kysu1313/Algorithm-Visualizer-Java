package models;

public class BinarySearchTree {

    private class Node extends Vertex{
        protected int key;
        protected Node left, right;

        public Node(int _value, Double _x, Double _y) {
            super(_x, _y);
            this.key = _value;
            this.left = this.right = null;
        }
    }

    private Node root;
    private Double x;
    private Double y;

    public BinarySearchTree(Double _x, Double _y) {
        this.x = _x;
        this.y = _y;
        this.root = null;
    }

    public void insert(int _value) {
        this.root = insertVal(this.root, _value);
    }

    private Node insertVal(Node _root, int _value) {
        if (_root == null) {
            _root = new Node(_value, this.x, this.y);
            return _root;
        }

        if (_value < _root.key) {
            root.left = insertVal(root.left, _value);
        } else if (_value > root.key) {
            root.right = insertVal(root.right, _value);
        }
        return root;
    }

    public void inorderTraversal(Node _root) {
        if (_root != null) {
            inorderTraversal(_root.left);
            System.out.println(_root.right);
            inorderTraversal(_root.right);
        }
    }



}
