package chess;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public abstract class ConnectScene extends Scene {
    public ConnectScene(URL location) throws IOException {
        this(FXMLLoader.load(location), 500, 300);
        GridPane root = (GridPane) this.getRoot();
        ((ComboBox) root.getChildren().get(1)).getItems().addAll("Воздушные", "Спиральные", "Кристал", "Классические");
        root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        ((Button) root.getChildren().get(6)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                back();
            }
        });
        ((Button) root.getChildren().get(7)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ((ImageView) root.getChildren().get(8)).setVisible(true);
                    find(((TextField) root.getChildren().get(3)).getText(),
                            Integer.parseInt(((TextField) root.getChildren().get(5)).getText()),
                            (String) ((ComboBox) root.getChildren().get(1)).getValue());
                } catch (Exception e) {

                }
            }
        });
        ((ImageView) root.getChildren().get(8)).setImage(new Image(new FileInputStream("loadGif.gif")));
        ((ImageView) root.getChildren().get(8)).setVisible(false);
    }

    public abstract void back();

    public abstract void find(String ip, int port, String theme) throws IOException;


    public ConnectScene(Parent root, double width, double height) {
        super(root, width, height);
    }
}
