package sample.game.engine.figure;

import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import sample.Xform;
import sample.field.Field;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Elephant extends Figure {
    public Elephant(Color c, int x, int y) {
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

            Cylinder c2 = new Cylinder(SIZE * 0.3, 50);
            c2.setTranslateY(32.5);
            c2.setMaterial(getMat());


            Cylinder c3 = new Cylinder(SIZE * 0.2, 60);
            c3.setTranslateY(82.5);
            c3.setMaterial(getMat());

            Sphere s = new Sphere(SIZE * 0.35);
            s.setTranslateY(120);
            s.setMaterial(getMat());

            res1.getChildren().addAll(c1, c2, c3, s);
            res = res1;
        }
        return res;
    }

    @Override
    public boolean canMove(int xto, int yto, Field field) {
        if (!(abs(xto - x) == abs(yto - y))) {
            return false;
        }
        int delx = xto - x;
        int dely = yto - y;
        delx = min(max(delx, -1), 1);
        dely = min(max(dely, -1), 1);
        boolean flag = true;
        for (int x = this.x + delx, y = this.y + dely; !(x == xto && y == yto); x += delx, y += dely) {
            if (field.figures[x][y] != null) {
                flag = false;
            }
        }
        return flag;
    }
}
