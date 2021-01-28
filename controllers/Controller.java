package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import models.*;
import models.blockPath.*;

import java.net.URL;
import java.security.Key;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.color;

public class Controller implements Initializable {

    public TextField numColumns;
    @FXML
    private AnchorPane graph;
    @FXML
    private AnchorPane sortGraph;
    @FXML
    private AnchorPane grid;
    @FXML
    private Slider scrollBar;
    @FXML
    private Label numLabel;
    @FXML
    private TextField numBars;
    @FXML
    private ComboBox comboBox;
    @FXML
    private TextArea textArea;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab graphTab;
    @FXML
    private Tab sortTab;
    @FXML
    private Tab gridTab;
    @FXML
    private VBox sideVBox;
    @FXML
    private ComboBox leftDropDown;
    @FXML
    private CheckBox allowHorizontals;

    private Vertex vertex1;
    private Vertex vertex2;
    private Vertex vertexDelete;
    private Arrow arrow;
    private BubbleSort bsort;
    private HeapSort hsort;
    private QuickSort qsort;
    private CoctailSort cSort;
    private InsertionSort iSort;
    private MyRectangle[] rects;
    private MyNode[][] nodes;
    private MyNode[] flatNodes;
    private MyNode startNode;
    private MyNode finishNode;
    private int rightClickCount;
    private String selectedSort;
    private String selectedGridSort;
    private boolean isGridActive;
    private ObservableMap<String, String> observableMap;
    private ObservableMap<String, String> observableGridMap;
    private int cols, rows;


    // Graph Events
    public void onGraphPressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            this.vertex1 = createAndAddVertex(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    public void onGraphDragDetected(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            this.vertex2 = createAndAddVertex(mouseEvent.getX(), mouseEvent.getY());
            this.arrow = createAndAddArrow(vertex1, vertex2);
            this.vertex2.getStyleClass().add("dragged");
            this.arrow.getStyleClass().add("dragged");
        }
    }

    public void onGraphDragged(MouseEvent mouseEvent) {
        if (this.vertex2 != null) {
            this.vertex2.setLayoutX(mouseEvent.getX());
            this.vertex2.setLayoutY(mouseEvent.getY());
        }
    }

    public void onGraphReleased(MouseEvent mouseEvent) {
        if (this.vertex2 != null) {
            this.vertex2.getStyleClass().remove("dragged");
            this.arrow.getStyleClass().remove("dragged");
        }
        this.vertex2 = null;
    }

    public void onGridDragged(MouseEvent mouseEvent) {
        if (null != this.flatNodes && null != this.grid) {
            MyNode node = Grid.getNode(this.flatNodes, mouseEvent.getX(), mouseEvent.getY());
            node.setNodeBackgroundColor("black", "black");
            node.setWall(true);
        }

    }

    public void onGridReleased(MouseEvent mouseEvent) {

    }

    /**
     * Clear all anchor panes
     * @param event
     */
    public void handleClear(ActionEvent event) {
        if (!this.graph.getChildren().isEmpty()) {
            this.graph.getChildren().clear();
        }
        if (!this.sortGraph.getChildren().isEmpty()) {
            this.sortGraph.getChildren().clear();
        }
        if (!this.grid.getChildren().isEmpty()) {
            this.grid.getChildren().clear();
        }
    }


    // Vertex Events

    /**
     * Used to determine when a vertex has been clicked.
     * @param mouseEvent
     * @param vertex
     */
    private void onVertexPressed(MouseEvent mouseEvent, Vertex vertex) {
        if (mouseEvent.isPrimaryButtonDown()) {
            vertex1 = vertex;
        } else if (mouseEvent.isSecondaryButtonDown()) {
            vertexDelete = vertex;
        }
    }


    /**
     * Used to determine when a vertex has been dragged.
     * @param mouseEvent
     * @param vertex
     */
    private void onVertexDragDetected(MouseEvent mouseEvent, Vertex vertex) {
        vertex.toFront();
        if (mouseEvent.isPrimaryButtonDown()) {
            this.vertex2 = createAndAddVertex(
                    vertex.getLayoutX() + mouseEvent.getX() + vertex.getTranslateX(),
                    vertex.getLayoutY() + mouseEvent.getY() + vertex.getTranslateY()
            );
            this.arrow = createAndAddArrow(vertex1, vertex2);
            this.vertex2.getStyleClass().add("dragged");
            this.arrow.getStyleClass().add("dragged");
        } else if (mouseEvent.isSecondaryButtonDown()) {
            this.vertexDelete = null;
            if (this.vertex2 != null) {
                for (Arrow a : vertex2.edges) {
                    a.getStyleClass().add("dragged");
                }
            }
        }
    }

    /**
     * Once a vertex has been dragged, compute the new X Y location.
     * @param e
     * @param vertex
     */
    private void onVertexDragged(MouseEvent e, Vertex vertex) {
        if (vertex2 != null) {
            vertex2.setLayoutX(vertex.getLayoutX() + e.getX() + vertex.getTranslateX());
            vertex2.setLayoutY(vertex.getLayoutY() + e.getY() + vertex.getTranslateY());
        }
        if (e.isPrimaryButtonDown()) {
            vertex.setLayoutX(vertex.getLayoutX() + e.getX() + vertex.getTranslateX());
            vertex.setLayoutY(vertex.getLayoutY() + e.getY() + vertex.getTranslateY());
        }

    }

    /**
     * Stop drag event.
     * @param mouseEvent
     * @param vertex
     */
    private void onVertexReleased(MouseEvent mouseEvent, Vertex vertex) {
        vertex.getStyleClass().remove("dragged");
        for (Arrow a : vertex.edges) {
            a.getStyleClass().remove("dragged");
        }
        if (this.vertex2 != null) {
            this.vertex2.getStyleClass().remove("dragged");
        }
        if (vertexDelete != null) {
            graph.getChildren().remove(vertexDelete);
            for (Arrow a : vertexDelete.edges) {
                graph.getChildren().remove(a);
            }
//            this.vertex1.setCount(this.vertex1.getCount() - 1);
        }
        vertex2 = null;
        vertexDelete = null;
    }


    // Helper Methods

    /**
     * Applies action events to each new vertex.
     * @param x
     * @param y
     * @return
     */
    private Vertex createAndAddVertex(Double x, Double y) {
        Vertex vertex = new Vertex(x, y);

        vertex.setOnAction(e -> {
            for (Arrow a : vertex.edges) {
                a.setHeadAVisible(!a.isHeadAVisible());
                a.setHeadBVisible(!a.isHeadBVisible());
            }
        });
        vertex.setOnMousePressed(mouseEvent -> onVertexPressed(mouseEvent, vertex));
        vertex.setOnDragDetected(mouseEvent -> onVertexDragDetected(mouseEvent, vertex));
        vertex.setOnMouseDragged(mouseEvent -> onVertexDragged(mouseEvent, vertex));
        vertex.setOnMouseReleased(mouseEvent -> onVertexReleased(mouseEvent, vertex));

        this.graph.getChildren().add(vertex);

        return vertex;
    }


    /**
     * Add arrow between two vertices.
     * @param v1
     * @param v2
     * @return
     */
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

    /**
     * Start button handler for binary tree and path-finding algorithms.
     * @param event
     * @throws InterruptedException
     */
    public void handleStart(ActionEvent event) throws InterruptedException {
        if (this.tabPane.getSelectionModel().getSelectedItem() == this.gridTab) {
            if (null == this.nodes) {
                makeGrid();
            }
            boolean horiz = false;
            if (this.allowHorizontals.isSelected()) {
                horiz = true;
            }

            switch (selectedGridSort) {
                case "Dijkstras":
                    this.textArea.setText(this.observableGridMap.get("Dijkstras"));
                    Dijkstras djk = new Dijkstras(this.nodes, this.startNode, this.finishNode, horiz);
                    break;
                case "A*":
                    this.textArea.setText(this.observableGridMap.get("A*"));
                    Astar astar = new Astar(this.nodes, this.startNode, this.finishNode, horiz);
                    break;
                default:
                    System.out.println("invalid selection");
                    break;
            }

        } else {
            System.out.println("start");
            BinaryTree bTree = new BinaryTree();
            bTree.createTree( 5, 10, this.graph);
        }

    }

    /**
     * Handle sort button for sort graph. Uses sorting algorithms.
     * @param event
     */
    public void handleSort(ActionEvent event) {
        if (null != this.selectedSort) {
            if (null == this.rects) {
                this.rects = new SortRectangles(this.sortGraph).getRectArr();
            }
            switch (this.selectedSort) {
                case "Bubble Sort":
                    this.textArea.setText(this.observableMap.get("Bubble Sort"));
                    this.bsort = new BubbleSort(this.rects, this.sortGraph);
                    this.bsort.sort();
                    break;
                case "Heap Sort":
                    this.textArea.setText(this.observableMap.get("Heap Sort"));
                    this.hsort = new HeapSort(this.rects, this.sortGraph);
                    this.hsort.sort();
                    break;
                case "Quick Sort":
                    this.textArea.setText(this.observableMap.get("Quick Sort"));
                    this.qsort = new QuickSort(this.rects, this.sortGraph);
                    break;
                case "Coctail Sort":
                    this.textArea.setText(this.observableMap.get("Coctail Sort"));
                    this.cSort = new CoctailSort(this.rects, this.sortGraph);
                    break;
                case "Insertion Sort":
                    this.textArea.setText(this.observableMap.get("Insertion Sort"));
                    this.iSort = new InsertionSort(this.rects, this.sortGraph);
                    break;
                default:
                    System.out.println("No sorting algorithm selected");
                    break;
            }
        }
    }

    /**
     * Create bars used in sorting algorithms.
     * @param event
     */
    public void handleMakeBars(ActionEvent event) {
        makeBars();
    }

    /**
     * Allow user input to specify the number of columns in a grid.
     */
    private void updateColumns() {
        EventHandler<ActionEvent> columnEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cols = Integer.parseInt(numColumns.getText());
                makeGrid();
            }
        };
        numColumns.setOnAction(columnEvent);
    }

    /**
     * Create new grid for path-finding and maze-building.
     */
    private void makeGrid() {
        this.graph.getChildren().clear();
        Grid grid = new Grid(this.cols, this.graph);
        this.nodes = grid.makeGrid();
        this.flatNodes = grid.getFlattenedNodes();
        int startRow = 3;
        int startCol = 3;

        this.startNode = this.nodes[startRow][startCol];
        this.startNode.setStart(true);
        this.finishNode = this.nodes[this.nodes.length-2][this.nodes[0].length-2];
        this.finishNode.setFinish(true);

        for (int i = 0; i < this.nodes.length; i++) {
            for (int j = 0; j < this.nodes[i].length; j++) {
                nodeAction(this.nodes[i][j]);
                this.grid.getChildren().add(this.nodes[i][j]);
            }
        }
    }

    /**
     * Handles make grid button event.
     * @param event
     */
    public void handleMakeGrid(ActionEvent event) {
        if (!this.numColumns.getText().isEmpty()){
            this.cols = Integer.parseInt(this.numColumns.getText());
        } else {
            this.cols = 30;
        }
        makeGrid();
    }

    /**
     * Creates a new vertical maze.
     * @param event
     */
    public void handleGenerateVerticalMaze(ActionEvent event) {
        if (null == this.nodes) {
            makeGrid();
        }
        MazeGenerator maze = new MazeGenerator(this.nodes, this.startNode, this.finishNode, true);
        maze.verticalMaze();
    }

    /**
     * Creates a new default maze.
     * @param event
     */
    public void handleGenerateMaze(ActionEvent event) {
        if (null == this.nodes) {
            makeGrid();
        }
        MazeGenerator maze = new MazeGenerator(this.nodes, this.startNode, this.finishNode, false);
    }

    /**
     * Begins the selected path-finding algorithm on current grid.
     * @param event
     */
    public void handleStartPathFinding(ActionEvent event) {
        if (this.tabPane.getSelectionModel().getSelectedItem() == this.gridTab) {
            if (null == this.nodes) {
                makeGrid();
            }
            boolean horiz = false;
            if (this.allowHorizontals.isSelected()) {
                horiz = true;
            }
            if (null == selectedGridSort) {
                selectedGridSort = "Dijkstras";
            }

            switch (selectedGridSort) {
                case "Dijkstras":
                    this.textArea.setText(this.observableGridMap.get("Dijkstras"));
                    Dijkstras djk = new Dijkstras(this.nodes, this.startNode, this.finishNode, horiz);
                    break;
                case "A*":
                    this.textArea.setText(this.observableGridMap.get("A*"));
                    Astar astar = new Astar(this.nodes, this.startNode, this.finishNode, horiz);
                    break;
                default:
                    System.out.println("invalid selection");
                    break;
            }

        } else {
            System.out.println("start");
            BinaryTree bTree = new BinaryTree();
//            bTree.createTree( 5, 10, this.graph);
        }
    }

    /**
     * Allows the creation of walls in the grid.
     * @param _node
     */
    private void nodeAction(MyNode _node) {
        _node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isSecondaryButtonDown() && !mouseEvent.isPrimaryButtonDown()) {
                    startNode.setStart(false);
                    _node.setStart(true);
                    startNode = _node;
                    _node.setNodeBackgroundColor("green", "black");
                }else if (mouseEvent.isPrimaryButtonDown() && mouseEvent.isSecondaryButtonDown()) {
                    finishNode.setFinish(false);
                    _node.setFinish(true);
                    finishNode = _node;
                } else if (_node.isWall()) {
                    _node.setNodeBackgroundColor("white", "black");
                } else {
                    _node.setNodeBackgroundColor("black", "black");
                }
                _node.setWall(!_node.isWall());
            }
        });
        _node.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (_node.isWall()) {
                    _node.setNodeBackgroundColor("white", "black");
                } else {
                    _node.setNodeBackgroundColor("black", "black");
                }
                _node.setWall(!_node.isWall());
            }
        });
    }

    private void makeBars() {
        if (!this.sortGraph.getChildren().isEmpty()) {
            this.sortGraph.getChildren().clear();
        }
        if (!this.numBars.getText().isEmpty()) {
            this.rects = new SortRectangles(Integer.parseInt(this.numBars.getText()), this.sortGraph).getRectArr();
            this.sortGraph.getChildren().addAll(this.rects);
        } else {
            this.rects = new SortRectangles(this.sortGraph).getRectArr();
            this.sortGraph.getChildren().addAll(this.rects);
        }
    }

    public void handleRandomize(ActionEvent event) {
        Grid.randomWalls(this.nodes);
    }

    private void scrollListener() {
        this.scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                if (null != rects) {
                    if (new_val.intValue() < 50) {
                        MyRectangle.setDuration(500 / (new_val.intValue()+1));
                        Astar.duration = 500 / (new_val.intValue()+1);
                        MazeGenerator.duration = 500 / (new_val.intValue()+1);
                    } else {
                        MyRectangle.setDuration(100 / new_val.intValue());
                        Astar.duration =100 / new_val.intValue();
                        MazeGenerator.duration = 100 / new_val.intValue();
                    }
                    System.out.println(MyRectangle.getDuration());
                }
            }
        });
        if (null != rects) {
            for (MyRectangle rect : this.rects) {
                rect.setDura(MyRectangle.getDuration());
            }
        }
    }

    public void handleComboBox(ActionEvent event) {
        System.out.println("cbox");
    }

    private void comboAction() {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedSort = comboBox.getValue().toString();
                System.out.println(selectedSort);
//                makeBars();
            }
        };
        EventHandler<ActionEvent> gridEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedGridSort = leftDropDown.getValue().toString();
                System.out.println(selectedGridSort);
//                makeGrid();
            }
        };
        this.comboBox.setOnAction(event);
        this.leftDropDown.setOnAction(gridEvent);
    }

    private void fillDescriptionMap() {
        this.observableMap = FXCollections.observableHashMap();
        this.observableMap.put("Bubble Sort", "Bubble Sort has an average of O(n**2) time complexity");
        this.observableMap.put("Heap Sort", "Heap Sort has an overall time complexity of O(n*log(n). Heapify is O(log(n)) and build heap is O(n).");
        this.observableMap.put("Quick Sort", "Quick Sort has average time complexity of O(n*log(n)).");
        this.observableMap.put("Coctail Sort", "Coctail Sort has a worst and average time complexity of O(n**2). Compared to Bubble Sort, Coctail Sort performs better, typically less than two times faster.");
        this.observableMap.put("Insertion Sort", "");

        this.observableGridMap = FXCollections.observableHashMap();
        this.observableGridMap.put("Dijkstras", "Dijkstras algorithm has a time complexity of O(V**2) where V is the number of vertices in the graph. If using a binary heap, the time can be reduced to O(E*log(V))");
        this.observableGridMap.put("A*", "A* (a-star) algorithm is similar to dijkstras but it uses a heuristic value to direct it's search towards the end node.");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.scrollBar.setBlockIncrement(10);
        this.cols = 0;
        scrollListener();
        fillDescriptionMap();
        updateColumns();
        this.comboBox.getItems().addAll(this.observableMap.keySet());
        this.leftDropDown.getItems().addAll(this.observableGridMap.keySet());
        comboAction();
    }
}


