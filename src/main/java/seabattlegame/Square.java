package seabattlegame;

import seabattlegui.SquareState;

public class Square {

    private SquareState squareState;

    Square() {
        squareState = SquareState.WATER;
    }

    public void setSquareState(SquareState squareState) {
        this.squareState = squareState;
    }

    public SquareState getSquareState() {
        return squareState;
    }

    public String toString() {
        return squareState.toString();
    }
}
