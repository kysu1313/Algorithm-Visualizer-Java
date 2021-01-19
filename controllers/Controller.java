package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import models.*;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.color;

public class Controller implements Initializable {

    @FXML
    private AnchorPane graph;
    @FXML
    private AnchorPane sortGraph;
    @FXML
    private Slider scrollBar;
    @FXML
    private Label numLabel;
    @FXML
    private TextField numBars;
    @FXML
    private ComboBox comboBox;
    private Vertex vertex1;
    private Vertex vertex2;
    private Vertex vertexDelete;
    private Arrow arrow;
    private BubbleSort bsort;
    private HeapSort hsort;
    private QuickSort qsort;
    private MyRectangle[] rects;
    private String selectedSort;
    private static final ObservableList<String> SORT_OPTIONS = FXCollections.observableArrayList(
            "Bubble Sort",
            "Heap Sort",
            "Quick Sort"
    );


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

    public void handleClear(ActionEvent event) {
        if (!this.graph.getChildren().isEmpty()) {
            this.graph.getChildren().clear();
        }
        if (!this.sortGraph.getChildren().isEmpty()) {
            this.sortGraph.getChildren().clear();
        }
    }


    // Vertex Events
    private void onVertexPressed(MouseEvent mouseEvent, Vertex vertex) {
        if (mouseEvent.isPrimaryButtonDown()) {
            vertex1 = vertex;
        } else if (mouseEvent.isSecondaryButtonDown()) {
            vertexDelete = vertex;
        }
//        this.vertexDelete = vertex;
    }

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
//                this.vertex2.getStyleClass().add("dragged");
                for (Arrow a : vertex2.edges) {
                    a.getStyleClass().add("dragged");
                }
            }
        }
    }

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

    public void handleStart(ActionEvent event) throws InterruptedException {
        System.out.println("start");
        BinaryTree bTree = new BinaryTree();

        bTree.createTree( 5, 10, this.graph);
    }

    public void handleSort(ActionEvent event) {
        if (null != this.selectedSort) {
            if (null == this.rects) {
                this.rects = new SortRectangles(this.sortGraph).getRectArr();
            }
            switch (this.selectedSort) {
                case "Bubble Sort":
                    this.bsort = new BubbleSort(this.rects, this.sortGraph);
                    this.bsort.sort();
                    break;
                case "Heap Sort":
                    this.hsort = new HeapSort(this.rects, this.sortGraph);
                    this.hsort.sort();
                    break;
                case "Quick Sort":
                    this.qsort = new QuickSort(this.rects, this.sortGraph);
                    break;
                default:
                    System.out.println("No sorting algorithm selected");
                    break;
            }
        }
    }

    public void handleMakeBars(ActionEvent event) {
        makeBars();
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

    private void scrollListener() {
        this.scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                if (null != rects) {
                    MyRectangle.setDuration(2000 / new_val.intValue());
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
                makeBars();
            }
        };
        this.comboBox.setOnAction(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.scrollBar.setBlockIncrement(10);
        scrollListener();
        this.comboBox.getItems().addAll(SORT_OPTIONS);
        comboAction();
    }
}


