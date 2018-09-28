package chess;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
    StartScene start;
    CreateGameScene create;
    ConnectScene connect;
    GameEngine game;
    Scene gameScene;
    String[] field;
    String opponentTheme;
    String yourTheme;
    int color;
    boolean fieldFlag = false;
    boolean colorFlag = false;
    boolean themeFlag = false;
    boolean openflag = false;

    String[] loadMap(String map) throws FileNotFoundException {
        if (map.equals("Стандартная Карта")) {
            return new String[]{"RHBQKBHR",
                    "PPPPPPPP",
                    "11111111",
                    "11111111",
                    "11111111",
                    "11111111",
                    "pppppppp",
                    "rhbqkbhr"};
        }
        Scanner in = new Scanner(new File(map));
        ArrayList<String> res = new ArrayList<>();
        while (in.hasNext()) {
            res.add(in.nextLine());
        }
        return res.toArray(new String[res.size()]);
    }

    Dialog dialog;

    public String convertTheme(String theme) {
        if (theme.equals("Воздушные")) {
            return "air";
        }
        if (theme.equals("Спиральные")) {
            return "spiral";
        }
        if (theme.equals("Кристал")) {
            return "cristal";
        }
        if (theme.equals("Классические")) {
            return "classic";
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        start = new StartScene(getClass().getResource("start.fxml")) {
            @Override
            public void newGame() {
                primaryStage.setScene(create);
            }

            @Override
            public void connectGame() {
                primaryStage.setScene(connect);
            }
        };
        create = new CreateGameScene(getClass().getResource("createGame.fxml"), primaryStage) {
            @Override
            public void back() {
                primaryStage.setScene(start);
            }

            @Override
            public void share(String map, String condition, int port, String theme) throws IOException {
                System.out.println(theme);
                ;
                yourTheme = convertTheme(theme);
                fieldFlag = colorFlag = true;
                themeFlag = openflag = false;
                dialog = Dialog.open(port, new Dialog.Handler() {
                    @Override
                    public void onMessage(String line) {
                        String[] words = line.split(" ");
                        if (words[0].equals("/theme")) {
                            opponentTheme = words[1];
                            themeFlag = true;
                        }
                        startGame(primaryStage);
                        handelGo(words);
                    }

                    @Override
                    public void onClosed() {

                    }
                });
                System.out.println("Connected");
                field = loadMap(map);
                String str = "";
                for (int i = 0; i < field.length; i++) {
                    str += field[i] + " ";
                }
                dialog.send("/field " + field.length + " " + str);
                int c = ((int) (Math.random() * 2)) * 255;
                dialog.send("/color " + c);
                dialog.send("/theme " + yourTheme);
                color = 255 - c;
                AlertColor();
            }
        };
        connect = new ConnectScene(getClass().getResource("connect.fxml")) {
            @Override
            public void back() {
                primaryStage.setScene(start);
            }

            @Override
            public void find(String ip, int port, String theme) throws IOException {
                System.out.println(theme);
                fieldFlag = themeFlag = colorFlag = openflag = false;
                System.out.println(ip + " " + port);
                yourTheme = convertTheme(theme);
                dialog = Dialog.connect(ip, port, new Dialog.Handler() {
                    @Override
                    public void onMessage(String line) {
                        String[] words = line.split(" ");
                        if (words[0].equals("/field")) {
                            int n = Integer.parseInt(words[1]);
                            field = new String[n];
                            for (int i = 2; i < n + 2; i++) {
                                field[i - 2] = words[i];
                            }
                            fieldFlag = true;
                        }
                        if (words[0].equals("/theme")) {
                            opponentTheme = words[1];
                            themeFlag = true;
                        }
                        if (words[0].equals("/color")) {
                            color = Integer.parseInt(words[1]);
                            AlertColor();
                            colorFlag = true;
                        }
                        handelGo(words);
                        startGame(primaryStage);

                    }

                    @Override
                    public void onClosed() {

                    }
                });

                dialog.send("/theme " + yourTheme);
            }
        };
        game = new GameEngine() {
            @Override
            public void send(int xfrom, int yfrom, int xto, int yto) {
                dialog.send("/go " + xfrom + " " + yfrom + " " + xto + " " + yto);
            }
        };
        gameScene = game.buildScene();
        primaryStage.setTitle("Chess");
        primaryStage.setScene(start);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    private void handelGo(String[] words) {
        if (words[0].equals("/go")) {
            int xform = Integer.parseInt(words[1]);
            int yform = Integer.parseInt(words[2]);
            int xto = Integer.parseInt(words[3]);
            int yto = Integer.parseInt(words[4]);
            System.out.println(xform + " " + yform + " " + xto + " " + yto);

            game.go(xform, yform, xto, yto);


        }
    }

    private void startGame(Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (colorFlag && themeFlag && fieldFlag && !openflag) {
                    openflag = true;
                    if (color == 255) {
                        game.biuldField(field, yourTheme, opponentTheme);
                    } else {
                        game.biuldField(field, opponentTheme, yourTheme);
                    }

                    primaryStage.setScene(gameScene);
                }
            }
        });
    }

    private void AlertColor() {
        if (color == 255) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Вы за белых");
                    a.showAndWait();
                }
            });
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Вы за черных");
                    a.showAndWait();
                }
            });
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
