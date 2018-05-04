package sample.game.engine.figure;

import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import sample.Xform;
import sample.field.Field;
import sample.game.engine.figure.Color;
import sample.game.engine.figure.Figure;

import static java.lang.Math.*;

public class Rook extends Figure {
    public Rook(Color c, int x, int y) {
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

            Cylinder c2 = new Cylinder(SIZE * 0.25, 80);
            c2.setTranslateY(47.5);
            c2.setMaterial(getMat());

            Cylinder c3 = new Cylinder(SIZE * 0.4, 40);
            c3.setTranslateY(107.5);
            c3.setMaterial(getMat());

            res1.getChildren().addAll(c1, c2, c3);
            res = res1;
        }
        return res;
    }


    @Override
    public boolean canMove(int xto, int yto, Field field) {
        if (!(xto == x || yto == y)) {
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
