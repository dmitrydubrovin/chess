
package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.field.Field;
import sample.view.*;
import sample.game.engine.*;

import java.io.File;
import java.io.FileNotFoundException;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        MainViewer mainViewer = new MainViewer();
        Field f = new Field(new File("bigbattle.chess"));
        GameViewer scene = new GameViewer(f);
        primaryStage.setTitle("Space chess");
        primaryStage.setScene(scene.scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
