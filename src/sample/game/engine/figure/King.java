package sample.game.engine.figure;

import javafx.css.Size;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import sample.Xform;
import sample.field.Field;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class King extends Figure {
    public King(Color c, int x, int y) {
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

            Cylinder c2 = new Cylinder(SIZE * 0.35, 20);
            c2.setTranslateY(25);
            c2.setMaterial(getMat());

            Cylinder c3 = new Cylinder(SIZE * 0.3, 60);
            c3.setTranslateY(65);
            c3.setMaterial(getMat());

            Cylinder c4 = new Cylinder(SIZE * 0.45, 10);
            c4.setTranslateY(100);
            c4.setMaterial(getMat());

            Cylinder c5 = new Cylinder(SIZE * 0.3, 30);
            c5.setTranslateY(120);
            c5.setMaterial(getMat());

            Box b1 = new Box(SIZE * 0.15, SIZE * 0.5, SIZE * 0.15);
            b1.setTranslateY(135 + SIZE * 0.2);
            b1.setMaterial(getMat());

            Box b2 = new Box(SIZE * 0.15, SIZE * 0.15, SIZE * 0.3);
            b2.setTranslateY(145 + SIZE * 0.1);
            b2.setMaterial(getMat());

            res1.getChildren().addAll(c1, c2, c3, c4, c5, b1, b2);
            res = res1;
        }
        return res;
    }

    @Override
    public boolean canMove(int xto, int yto, Field field) {

        int delx = xto - x;
        int dely = yto - y;
        return abs(delx) < 2 && abs(dely) < 2;
    }
}
