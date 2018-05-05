package sample.game.engine.figure;


import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;
import sample.field.Field;
import sample.game.engine.Change;


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
        ph.setDiffuseColor(c == Color.WHITE ? javafx.scene.paint.Color.WHITE : javafx.scene.paint.Color.rgb(30, 30, 30));
        return ph;
    }

    public static Figure valueOf(int x, int y, Color cur, String str) {
        switch (str) {
            case "Ферзь": {
                return new Queen(cur, x, y);
            }
            case "Конь": {
                return new Horse(cur, x, y);
            }
            case "Ладья": {
                return new Rook(cur, x, y);
            }
            case "Слон": {
                return new Elephant(cur, x, y);
            }
        }
        return null;
    }
}
