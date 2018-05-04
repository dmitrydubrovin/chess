package sample.game.engine.figure;

import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import sample.Xform;
import sample.field.Field;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Queen extends Figure {
    public Queen(Color c, int x, int y) {
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


            Sphere s = new Sphere(SIZE * 0.2);
            s.setTranslateY(140 + SIZE * .1);
            s.setMaterial(getMat());

            res1.getChildren().addAll(c1, c2, c3, c4, c5, s);
            res = res1;
        }
        return res;
    }

    @Override
    public boolean canMove(int xto, int yto, Field field) {
        if (!(xto == x || yto == y||(abs(xto - x) == abs(yto - y)))) {
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
