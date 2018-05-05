package sample.game.engine.figure;

import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import sample.*;
import javafx.scene.Node;
import sample.field.Field;
import sample.game.engine.figure.Color;
import sample.game.engine.figure.Figure;

import static java.lang.Math.abs;


public class FootMan extends Figure {
    public FootMan(Color c, int x, int y) {
        super(c, x, y);
    }

    public boolean start = true;
    Node res = null;

    @Override
    public Node toNode() {
        if (res == null) {
            Xform res1 = new Xform();

            Cylinder c1 = new Cylinder(SIZE * 0.4, 15);
            c1.setTranslateY(7.5);
            c1.setMaterial(getMat());

            Cylinder c2 = new Cylinder(SIZE * 0.3, 40);
            c2.setTranslateY(27.5);
            c2.setMaterial(getMat());


            Cylinder c3 = new Cylinder(SIZE * 0.2, 50);
            c3.setTranslateY(52.5);
            c3.setMaterial(getMat());

            Sphere s = new Sphere(SIZE * 0.35);
            s.setTranslateY(95);
            s.setMaterial(getMat());

            res1.getChildren().addAll(c1, c2, c3, s);
            res = res1;
        }
        return res;
    }

    @Override
    public boolean canMove(int xto, int yto, Field field) {
        if (canMove0(xto, yto, field)) {
            start = false;
            return true;
        }
        return false;
    }

    public boolean canMove0(int xto, int yto, Field field) {
        if (xto == x && yto == y) {
            return true;
        }
        if (c == Color.WHITE) {
            if (!(xto == x - 1 || (start && xto == x - 2))) {
                return false;
            }
            if (abs(yto - y) > 1) {
                return false;
            }
            if (yto == y) {
                return field.space[xto][yto] == '1';
            }
            return field.space[xto][yto] != '1' &&
                    Character.toUpperCase(field.space[xto][yto]) == (field.space[xto][yto]);

        } else {
            if (!(xto == x + 1 || (start && xto == x + 2))) {
                return false;
            }
            if (abs(yto - y) > 1) {
                return false;
            }
            if (yto == y) {
                return field.space[xto][yto] == '1';
            }
            return field.space[xto][yto] != '1' &&
                    Character.toLowerCase(field.space[xto][yto]) == (field.space[xto][yto]);

        }
    }

}
