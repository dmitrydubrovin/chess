package sample.game.engine.figure;


import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import sample.field.Field;


public abstract class Figure {
    public Color c;
    public int x, y;

    protected static final int SIZE = 80;

    public Figure(Color c, int x, int y) {
        this.c = c;
        this.x = x;
        this.y = y;
    }

    public abstract Node toNode();

    public abstract boolean canMove(int xto, int yto, Field field);


    public PhongMaterial getMat() {
        PhongMaterial ph = new PhongMaterial();
        ph.setDiffuseColor(c == Color.WHITE ? javafx.scene.paint.Color.WHITE : javafx.scene.paint.Color.rgb(30,30,30));
        return ph;
    }
}
