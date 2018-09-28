package chess;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;

public abstract class StartScene extends Scene {
    public StartScene(URL location) throws IOException {
        this(FXMLLoader.load(location),500,300);
        GridPane root = (GridPane) this.getRoot();
        root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE,CornerRadii.EMPTY,Insets.EMPTY)));
        ((Button)root.getChildren().get(0)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newGame();
            }
        });
        ((Button)root.getChildren().get(1)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connectGame();
            }
        });
    }

    public abstract void newGame();
    public abstract void connectGame();

    public StartScene(Parent root, double width, double height) {
        super(root, width, height);
    }
}
