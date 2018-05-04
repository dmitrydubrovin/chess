package sample.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class MainViewer {
    public Scene scene;

    public MainViewer() {
        Pane root = new Pane();

        TextField nickName = new TextField();
        nickName.setPrefWidth(400);
        nickName.setPrefHeight(50);
        nickName.setTranslateX(50);
        nickName.setTranslateY(150);
        nickName.setPromptText("Какое у тебя имя?");
        nickName.setId("nick name field");

        TextField gameKey = new TextField();
        gameKey.setPrefWidth(200);
        gameKey.setPrefHeight(50);
        gameKey.setTranslateX(50);
        gameKey.setTranslateY(225);
        gameKey.setPromptText("Код от игры");
        gameKey.setId("game code field");

        Button connect = new Button("Подключиться к игре");
        connect.setPrefWidth(200);
        connect.setPrefHeight(50);
        connect.setTranslateX(250);
        connect.setTranslateY(225);
        nickName.setId("game connect button");

        Button newGame = new Button("Создать игру");
        newGame.setPrefWidth(400);
        newGame.setPrefHeight(50);
        newGame.setTranslateX(50);
        newGame.setTranslateY(300);
        newGame.setId("new game button");

        root.getChildren().addAll(nickName, gameKey, connect, newGame);
        scene = new Scene(root, 500, 500);
    }
}
