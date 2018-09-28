package chess;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public abstract class CreateGameScene extends Scene {
    public CreateGameScene(URL location, Stage primaryStage) throws IOException {
        this(FXMLLoader.load(location), 500, 300);
        GridPane root = (GridPane) this.getRoot();
        root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        ((ComboBox) root.getChildren().get(8)).getItems().addAll("Воздушные", "Спиральные", "Кристал", "Классические");
        ((Button) root.getChildren().get(9)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                back();
            }
        });
        ((Button) root.getChildren().get(10)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    String theme = (String) (((ComboBox) root.getChildren().get(8)).getValue());
                    System.out.println(theme);
                    ;
                    share(((Label) root.getChildren().get(1)).getText(),
                            (String) ((ComboBox) root.getChildren().get(4)).getValue(),
                            Integer.parseInt(((TextField) root.getChildren().get(6)).getText()),
                            theme);
                    ((ImageView) root.getChildren().get(9)).setVisible(true);
                } catch (Exception e) {

                }
            }
        });
        FileChooser fileChooser = new FileChooser();
        ((Button) root.getChildren().get(2)).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    ((Label) root.getChildren().get(1)).setText(file.getAbsolutePath());
                }

            }
        });
        ((ComboBox) root.getChildren().get(4)).getItems().addAll("Съесть короля", "Съесть все фигуры");
        ((ImageView) root.getChildren().get(11)).setImage(new Image(new FileInputStream("loadGif.gif")));
        ((ImageView) root.getChildren().get(11)).setVisible(false);
    }

    public abstract void back();

    public abstract void share(String map, String condition, int port, String theme) throws IOException;

    public CreateGameScene(Parent root, double width, double height) {
        super(root, width, height);
    }
}
