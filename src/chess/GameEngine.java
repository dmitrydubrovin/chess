
package chess;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * @author cmcastil
 */
public abstract class GameEngine {

    final Group root = new Group();
    final Xform axisGroup = new Xform();
    final Xform moleculeGroup = new Xform();
    final Xform world = new Xform();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 250.0;
    private static final double HYDROGEN_ANGLE = 104.5;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;
    private StlMeshImporter importer = new StlMeshImporter();

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    //   private void buildScene() {
    //       root.getChildren().add(world);
    //   }
    private void buildCamera() {
        System.out.println("buildCamera()");
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void buildAxes() {
        System.out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(false);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * MOUSE_SPEED * modifier * ROTATION_SPEED);
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * MOUSE_SPEED * modifier * ROTATION_SPEED);
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * MOUSE_SPEED * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
                    cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
                }
            }
        });
    }

    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        cameraXform2.t.setX(0.0);
                        cameraXform2.t.setY(0.0);
                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
                        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
                        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case V:
                        moleculeGroup.setVisible(!moleculeGroup.isVisible());
                        break;
                }
            }
        });
    }

    public Xform buildFigure(String name, Color c, String theme) {
        importer.read(new File("models\\" + theme + "\\" + name + ".STL"));
        TriangleMesh tr = importer.getImport();
        PhongMaterial ph = new PhongMaterial(c);
        Xform x = new Xform();
        MeshView ms = new MeshView(tr);
        ms.setMaterial(ph);
        x.getChildren().addAll(ms);
        x.setRotateX(90);
        world.getChildren().addAll(x);
        //fix model bugs
        if (theme.equals("cristal")) {
            if (name.equals("Knight")) {
                ms.setTranslateX(-175);
            }
            if (name.equals("Rook")) {
                ms.setTranslateX(-70);
            }
            if (name.equals("Bishop")) {
                ms.setTranslateX(-35);
            }
            if (name.equals("Queen")) {
                ms.setTranslateX(-140);
            }
            if (name.equals("King")) {
                ms.setTranslateX(-105);
            }
        }
        if (theme.equals("air")) {
            if (!name.equals("Pawn")) {
                x.setRotateX(180);
                ms.setTranslateX(-17);
                ms.setTranslateZ(+17);
            } else {
                ms.setTranslateX(-90);
                ms.setTranslateY(85);
            }
        }
        if (theme.equals("classic")) {
            if (name.equals("Knight")) {
                ms.setTranslateX(-25);
                ms.setTranslateY(-75);
            }
            if (name.equals("Rook")) {
                ms.setTranslateX(-35);
                ms.setTranslateY(-20);
            }
            if (name.equals("Bishop")) {
                ms.setTranslateX(-125);
                ms.setTranslateY(-75);
            }
            if (name.equals("Queen")) {
                ms.setTranslateX(+40);
                ms.setTranslateY(-25);
            }
            if (name.equals("King")) {
                ms.setTranslateX(-75);
                ms.setTranslateY(-75);
            }
            if (name.equals("Pawn")) {
                ms.setTranslateX(+50);
                ms.setTranslateY(-75);
            }
        }
        if (theme.equals("spiral")) {
            if (name.equals("Knight")) {
                ms.setTranslateZ(-12);
                ms.setTranslateX(-17);
                ms.setTranslateY(30);
            }
            if (name.equals("Rook")) {
                ms.setTranslateX(-17);
                ms.setTranslateY(30);
            }
            if (name.equals("Bishop")) {
                ms.setTranslateZ(-16);
                ms.setTranslateX(-20);
                ms.setTranslateY(35);
            }
            if (name.equals("Queen")) {
                ms.setTranslateZ(-20);
                ms.setTranslateX(-20);
                ms.setTranslateY(43);
            }
            if (name.equals("King")) {
                ms.setTranslateZ(-24);
                ms.setTranslateX(-20);
                ms.setTranslateY(50);
            }
            if (name.equals("Pawn")) {
                ms.setTranslateZ(-8);
                ms.setTranslateX(-15);
                ms.setTranslateY(25);
            }
        }
        return x;
    }

    double x, z;
    Xform[][] map;
    Box[][] boxfield;
    char[][] tactic;

    public void biuldField(String[] field, String whiteTheme, String blackTheme) {
        world.getChildren().clear();
        int n = field.length;
        int m = 0;
        for (int i = 0; i < field.length; i++) {
            m = max(m, field[i].length());
        }
        map = new Xform[n][m];
        tactic = new char[n][m];
        boxfield = new Box[n][m];
        System.out.println(n + " " + m);
        x = (n - 1) * 50.0 / 2;
        z = (m - 1) * 50.0 / 2;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                tactic[i][j] = field[i].charAt(j);

                if (field[i].charAt(j) == '0') {
                    continue;
                }

                Box b = new Box(50, 2, 50);
                b.setTranslateY(-2);
                b.setTranslateX(-x + i * 50);
                b.setTranslateZ(-z + j * 50);
                boxfield[i][j] = b;
                if ((i + j) % 2 == 0) {
                    b.setMaterial(new PhongMaterial(Color.BLACK));
                } else {
                    b.setMaterial(new PhongMaterial(Color.WHITE));
                }
                world.getChildren().addAll(b);

                int finalI = i;
                int finalJ = j;
                b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        click(finalI, finalJ);
                    }
                });

                if (field[i].charAt(j) == '1') {
                    continue;
                }

                String name = "";
                switch (Character.toLowerCase(tactic[i][j])) {
                    case 'r': {
                        name = "Rook";
                        break;
                    }
                    case 'h': {
                        name = "Knight";
                        break;
                    }
                    case 'k': {
                        name = "King";
                        break;
                    }
                    case 'b': {
                        name = "Bishop";
                        break;
                    }
                    case 'q': {
                        name = "Queen";
                        break;
                    }
                    case 'p': {
                        name = "Pawn";
                        break;
                    }
                }

                Color c = Color.WHITE;
                double ang = 90;
                String theme = whiteTheme;
                if (tactic[i][j] == Character.toLowerCase(tactic[i][j])) {
                    c = Color.rgb(75, 75, 75);
                    ang *= -1;
                    theme = blackTheme;
                }

                map[i][j] = buildFigure(name, c, theme);
                map[i][j].t.setX(-x + i * 50);
                map[i][j].t.setZ(-z + j * 50);
                map[i][j].setRotateY(ang);

                map[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        click(finalI, finalJ);
                    }
                });

            }
        }
    }

    int lastX, lastY;
    int q = 0;

    public void click(int x, int y) {
        System.out.println(q + " " + x + " " + y);
        if (q == 0) {
            if (tactic[x][y] == '1') {
                return;
            }
            lastX = x;
            lastY = y;
            q = 1;
            boxfield[x][y].setMaterial(new PhongMaterial(Color.GREEN));
        } else {
            q = 0;
            go(lastX, lastY, x, y);
            boxfield[lastX][lastY].setMaterial(new PhongMaterial((lastX + lastY)%2 == 0 ? Color.BLACK : Color.WHITE));
            send(lastX, lastY, x, y);
        }
    }

    public void go(int xfrom, int yfrom, int xto, int yto) {
        tactic[xto][yto] = tactic[xfrom][yfrom];
        tactic[xfrom][yfrom] = 1;
        if (map[xto][yto] != null) {
            Xform a = map[xto][yto];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    world.getChildren().remove(a);
                }
            });
        }
        map[xto][yto] = map[xfrom][yfrom];
        map[xfrom][yfrom] = null;

        map[xto][yto].t.setX(-x + xto * 50);
        map[xto][yto].t.setZ(-z + yto * 50);
        map[xto][yto].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                click(xto, yto);
            }
        });

    }

    public Scene buildScene() {

        // setUserAgentStylesheet(STYLESHEET_MODENA);

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        // buildScene();
        buildCamera();
        buildAxes();

        Scene scene = new Scene(root, 600, 400, true);
        scene.setFill(Color.GREY);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        scene.setCamera(camera);
        return scene;
    }

    public abstract void send(int xfrom, int yfrom, int xto, int yto);
}
