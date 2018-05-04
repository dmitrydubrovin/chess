package sample.game.engine;

import sample.game.engine.figure.Figure;

public class Change {
    int x, y;
    Figure figure;

    public Change(int x, int y, Figure figure) {
        this.x = x;
        this.y = y;
        this.figure = figure;
    }
}
