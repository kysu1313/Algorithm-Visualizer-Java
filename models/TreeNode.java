package models;

public class TreeNode extends Vertex {

    private TreeNode leftChild;
    private TreeNode rightChild;
    private TreeNode parent;
    private double x;
    private double y;
    private double value;

    public TreeNode(Double _value, Double _x, Double _y, TreeNode _leftChild, TreeNode _rightChild, TreeNode _parent) {
        super(_x, _y);
        this.value = _value;
        this.x = _x;
        this.y = _y;
        this.leftChild = _leftChild;
        this.rightChild = _rightChild;
        this.parent = _parent;
    }

    public TreeNode getLeftChild() {
        return this.leftChild;
    }

    public TreeNode getRightChild() {
        return this.rightChild;
    }

    public TreeNode getParentNode() {
        return this.parent;
    }

    public void setLeftChild(TreeNode _rightChild) {
        this.rightChild = _rightChild;
    }

    public void setRightChild(TreeNode _leftChild) {
        this.leftChild = _leftChild;
    }

    public void setParentNode(TreeNode _parent) {
        this.parent = _parent;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
