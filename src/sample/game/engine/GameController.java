package sample.game.engine;

import sample.field.Field;

public class GameController {
    Field field;

    public GameController(Field field) {
        this.field = field;
        field.gc = this;
    }

    public void change(Change... changes) {
        for (Change ch : changes) {
            field.getChildren().remove(field.figures[ch.x][ch.y].toNode());
            if (ch.figure != null) {
                field.getChildren().addAll(ch.figure.toNode());
            }
            field.figures[ch.x][ch.y] = ch.figure;
        }
    }

    public void onTap() {

    }

}
