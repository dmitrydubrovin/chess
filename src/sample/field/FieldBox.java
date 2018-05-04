package sample.field;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class FieldBox extends Box {
    private static final int SIZE = 80;
    private static final int HEIGHT = 10;
    protected static int FIELD_SIZE_X = 8;
    protected static int FIELD_SIZE_Z = 8;
    final int x, z;

    private static final PhongMaterial WHITE = new PhongMaterial();
    private static final PhongMaterial BLACK = new PhongMaterial();

    static {
        WHITE.setDiffuseColor(Color.WHITE);
        BLACK.setDiffuseColor(Color.BLACK);
    }

    public FieldBox(int x, int z) {
        super(SIZE, HEIGHT, SIZE);
        this.x = x;
        this.z = z;
        setTranslateY(-HEIGHT / 2);
        setTranslateX(x * SIZE + SIZE / 2 - FIELD_SIZE_X * SIZE / 2);
        setTranslateZ(z * SIZE + SIZE / 2 - FIELD_SIZE_Z * SIZE / 2);
        if ((x + z) % 2 == 0) {
            setMaterial(WHITE);
        } else {
            setMaterial(BLACK);
        }
    }
}
