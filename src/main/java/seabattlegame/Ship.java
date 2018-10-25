package seabattlegame;

import seabattlegui.ShipType;
import seabattlegui.SquareState;

import java.awt.*;

public class Ship {

    private ShipType shipType;
    private int shipLength;
    private Point bowPos;
    private boolean horizontal;
    private Square[] squaresHit;

    Ship(ShipType shipType) {
        this.shipType = shipType;
        this.shipLength = calculateShipLength();
        squaresHit = new Square[shipLength];
        fillSquaresArray();
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setBowPos(Point bowPos) {
        this.bowPos = bowPos;
    }

    public Point getBowPos() {
        return bowPos;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setShipLength(int shipLength) {
        this.shipLength = shipLength;
    }

    public int getShipLength() {
        return shipLength;
    }

    private int calculateShipLength() {
        switch (shipType) {
            case AIRCRAFTCARRIER:
                return 5;
            case BATTLESHIP:
                return 4;
            case CRUISER:
                return 3;
            case SUBMARINE:
                return 3;
            case MINESWEEPER:
                return 2;
            default:
                return 0;
        }
    }

    private void fillSquaresArray() {
        for (int i = 0; i < squaresHit.length; i++) {
            squaresHit[i] = new Square();
            squaresHit[i].setSquareState(SquareState.SHIP);
        }
    }

    public void shipIsHit(int x, int y) {
        int squareNr;
        boolean shipSunk = true;
        if (horizontal) {
            squareNr = x - bowPos.x;
        } else {
            squareNr = y - bowPos.y;
        }
        // DEBUG PRINT: System.out.println("X = " + x +  " Y = " + y +" squareNr = " + squareNr);
        squaresHit[squareNr].setSquareState(SquareState.SHOTHIT);
        for (Square square : squaresHit) {
            if (square.getSquareState() == SquareState.SHIP) {
                shipSunk = false;
                break;
            }
        }
        if (shipSunk) {
            for (Square square : squaresHit) {
                square.setSquareState(SquareState.SHIPSUNK);
            }
        }
    }

    public boolean shipIsSunk() {
        return (squaresHit[0].getSquareState() == SquareState.SHIPSUNK);
    }


}
