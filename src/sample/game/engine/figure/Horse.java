package sample.game.engine.figure;

import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import sample.Xform;
import sample.field.Field;
import sample.game.engine.figure.Color;
import sample.game.engine.figure.Figure;

import java.util.Arrays;

import static java.lang.Math.abs;

public class Horse extends Figure {
    public Horse(Color c, int x, int y) {
        super(c, x, y);
    }

    Node res = null;

    @Override
    public Node toNode() {
        if (res == null) {
            Xform res1 = new Xform();

            Cylinder c1 = new Cylinder(SIZE * 0.4, 15);
            c1.setTranslateY(7.5);
            c1.setMaterial(getMat());


            Box b1 = new Box(SIZE * 0.55, 30, SIZE * 0.4);
            b1.setTranslateY(22.5);
            b1.setMaterial(getMat());

            Box b2 = new Box(SIZE * 0.45, 15, SIZE * 0.4);
            b2.setTranslateY(45);
            b2.setTranslateX(SIZE * 0.05);
            b2.setMaterial(getMat());

            Box b3 = new Box(SIZE * 0.35, 15, SIZE * 0.4);
            b3.setTranslateY(60);
            b3.setTranslateX(SIZE * 0.1);
            b3.setMaterial(getMat());

            Box b4 = new Box(SIZE * 0.6, 30, SIZE * 0.4);
            b4.setTranslateY(82.5);
            b4.setTranslateX(-SIZE * 0.025);
            b4.setMaterial(getMat());

            Box b5 = new Box(SIZE * 0.2, 16, SIZE * 0.2);
            b5.setTranslateY(105.5);
            b5.setTranslateX(SIZE * 0.15);
            b5.setMaterial(getMat());

            res1.getChildren().addAll(c1, b1, b2, b3, b4, b5);
            if (c == Color.BLACk) {
                res1.setRotateY(180);
            }
            res = res1;
        }
        return res;
    }

    @Override
    public boolean canMove(int xto, int yto, Field field) {
        int[] del = new int[]{abs(x - xto), abs(y - yto)};
        Arrays.sort(del);
        return del[0] == 1 && del[1] == 2;
    }
}
