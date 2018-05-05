package sample.field;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.PhongMaterial;
import sample.Xform;
import sample.game.engine.*;
import sample.game.engine.figure.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Field extends Xform {
    public char[][] space;
    public FieldBox[][] field;
    public Figure[][] figures;
    public boolean selected = false;
    public Figure selectFigure = null;
    public int sx, sy;
    public GameController gc;

    public Field(InputStream map) throws FileNotFoundException {
        buildField(new Scanner(map));
    }

    public void buildField(Scanner in) {
        ArrayList<char[]> tmp = new ArrayList<>();
        while (in.hasNext()) {
            String line = in.nextLine();
            tmp.add(line.toCharArray());
        }

        space = new char[tmp.size()][];
        int h = 0;
        int v = tmp.size();
        int ind = 0;
        for (char[] b : tmp) {
            space[ind++] = b;
            h = Math.max(h, b.length);
        }
        field = new FieldBox[v][h];
        figures = new Figure[v][h];
        FieldBox.FIELD_SIZE_X = v;
        FieldBox.FIELD_SIZE_Z = h;
        for (int x = 0; x < space.length; x++) {
            for (int z = 0; z < space[x].length; z++) {
                if (space[x][z] != '0') {
                    addBox(x, z);
                    final int finalX = x;
                    final int finalZ = z;
                    field[x][z].setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            onTap(finalX, finalZ);
                        }
                    });
                    if (space[x][z] == '1') {
                        continue;
                    }
                    Figure cur = null;
                    if ((space[x][z]) == 'f') {
                        cur = new FootMan(Color.WHITE, x, z);
                    } else if (space[x][z] == 'F') {
                        cur = new FootMan(Color.BLACk, x, z);
                    } else if ((space[x][z]) == 'r') {
                        cur = new Rook(Color.WHITE, x, z);
                    } else if (space[x][z] == 'R') {
                        cur = new Rook(Color.BLACk, x, z);
                    } else if ((space[x][z]) == 'h') {
                        cur = new Horse(Color.WHITE, x, z);
                    } else if (space[x][z] == 'H') {
                        cur = new Horse(Color.BLACk, x, z);
                    } else if ((space[x][z]) == 'e') {
                        cur = new Elephant(Color.WHITE, x, z);
                    } else if (space[x][z] == 'E') {
                        cur = new Elephant(Color.BLACk, x, z);
                    } else if ((space[x][z]) == 'q') {
                        cur = new Queen(Color.WHITE, x, z);
                    } else if (space[x][z] == 'Q') {
                        cur = new Queen(Color.BLACk, x, z);
                    } else if ((space[x][z]) == 'k') {
                        cur = new King(Color.WHITE, x, z);
                    } else if (space[x][z] == 'K') {
                        cur = new King(Color.BLACk, x, z);
                    }
                    Node cn = cur.toNode();
                    move(cn, x, z);
                    getChildren().addAll(cn);
                    final Figure finalCur = cur;
                    cn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            onTap(finalCur.x, finalCur.y);
                        }
                    });
                    figures[x][z] = cur;
                }
            }
        }
    }

    private void addBox(int x, int z) {
        field[x][z] = new FieldBox(x, z);
        getChildren().add(field[x][z]);
    }

    private void move(Node node, int x, int z) {
        node.setTranslateX(field[x][z].getTranslateX());
        node.setTranslateZ(field[x][z].getTranslateZ());
    }

    private void move(Figure figure, int x, int z, boolean isYourTurn) {
        if (figures[x][z] != null) {
            getChildren().remove(figures[x][z]);
        }
        space[x][z] = space[figure.x][figure.y];
        space[figure.x][figure.y] = '1';

        figures[figure.x][figure.y] = null;
        if (figures[x][z] != null) {
            getChildren().remove(figures[x][z].toNode());
        }
        figure.x = x;
        figure.y = z;
        move(figure.toNode(), x, z);
        figure.toNode().setTranslateX(field[x][z].getTranslateX());
        figure.toNode().setTranslateZ(field[x][z].getTranslateZ());
        figures[figure.x][figure.y] = figure;
        if (figure instanceof FootMan) {
            if ((figure.c == Color.WHITE && (x == 0 || space[x - 1][z] == '0')) ||
                    (figure.c == Color.BLACk && (x == field.length - 1 || space[x + 1][z] == '0'))) {
                Figure f = Figure.valueOf(x, z, figure.c, alert("Во что превратить пешку?", "Ферзь", "Конь", "Ладья", "Слон"));
                getChildren().remove(figures[x][z].toNode());
                f.toNode().setTranslateX(field[x][z].getTranslateX());
                f.toNode().setTranslateZ(field[x][z].getTranslateZ());
                figures[f.x][f.y] = f;
                getChildren().add(f.toNode());
                final Figure finalCur = f;
                f.toNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        onTap(finalCur.x, finalCur.y);
                    }
                });
            }
        }
    }

    private void onTap(int x, int z) {
        if (selected) {
            if (selectFigure.canMove(x, z, this)) {
                move(selectFigure, x, z, true);
                PhongMaterial mat = new PhongMaterial();
                mat.setDiffuseColor((sx + sy) % 2 == 0 ? javafx.scene.paint.Color.WHITE : javafx.scene.paint.Color.BLACK);
                field[sx][sy].setMaterial(mat);
                selected = false;
            } else {
                alert("Сюда нельзя ходить", "Ок");
            }
        } else {
            if (figures[x][z] != null) {
                sx = x;
                sy = z;

                PhongMaterial mat = new PhongMaterial();
                mat.setDiffuseColor(javafx.scene.paint.Color.GREEN);
                field[x][z].setMaterial(mat);

                selected = true;
                selectFigure = figures[x][z];
            } else {
                alert("Выберите клетку с фигурой", "Ок");
            }
        }
    }

    private String alert(String txt, String... buttons) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Сообщение");
        alert.setHeaderText(null);
        alert.setContentText(txt);
        ButtonType[] types = new ButtonType[buttons.length];
        for (int i = 0; i < buttons.length; i++) {
            types[i] = new ButtonType(buttons[i]);
        }
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(types);
        return alert.showAndWait().get().getText();
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                res += space[i][j];
            }
            res += "\n";
        }
        return res;
    }
}
