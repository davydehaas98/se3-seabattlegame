package seabattlegame;

import seabattlegui.ShipType;
import seabattlegui.ShotType;
import seabattlegui.SquareState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Grid {

    private Square[][] squares;
    private ArrayList<Ship> allShips;
    private int numberOfSunkenShips;

    Grid() {
        squares  = new Square[10][10];
        allShips = new ArrayList<>();
        numberOfSunkenShips = 0;
        fillGridWithSquares();
    }

    public ArrayList<Ship> getAllShips() {
        return allShips;
    }

    private void fillGridWithSquares() {
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                squares[i][j] = new Square();
            }
        }
    }

    public void addShip(Ship ship, int bowX, int bowY, boolean horizontal) {
        Point bowPos = new Point(bowX, bowY);
        ship.setBowPos(bowPos);
        ship.setHorizontal(horizontal);
        allShips.add(ship);
    }

    public Ship checkForShip (int x, int y) {
        for(Ship ship : allShips) {
            Point bowPos = ship.getBowPos();
            if(ship.isHorizontal()) {
                // -1 is added to the equation to compensate for the fact that the checked square is
                // also part of the ship, and therefore you only have the add the remaining length to
                // reach the far end of the ship
                if(bowPos.x <= x && x <= bowPos.x + ship.getShipLength() -1 && bowPos.y == y) {
                    return ship;
                }
            }
            else {
                // -1 is added to the equation to compensate for the fact that the checked square is
                // also part of the ship, and therefore you only have the add the remaining length to
                // reach the far end of the ship
                if(bowPos.x == x && bowPos.y <= y && y<= bowPos.y + ship.getShipLength() - 1) {
                    return ship;
                }
            }
        }
        return null;
    }

    public boolean checkShipTypePlaced(ShipType shipType){
        for(Ship existingShip : allShips) {
            if ( existingShip.getShipType() == shipType) {
                return true;
            }
        }
        return false;
    }

    public ShotType checkShotState(int x, int y) {
        boolean shipIsHit = false;
        for(Ship ship : allShips) {
            Point bowPos = ship.getBowPos();
            if(ship.isHorizontal()) {
                // -1 is added to the equation to compensate for the fact that the checked square is
                // also part of the ship, and therefore you only have the add the remaining length to
                // reach the far end of the ship
                if(bowPos.x <= x && x <= bowPos.x + ship.getShipLength() -1 && bowPos.y == y) {
                    // Ship is hit
                    shipIsHit = true;
                }
            }
            else {
                // -1 is added to the equation to compensate for the fact that the checked square is
                // also part of the ship, and therefore you only have the add the remaining length to
                // reach the far end of the ship
                if(bowPos.x == x && bowPos.y <= y &&  y <= bowPos.y + ship.getShipLength() - 1) {
                    shipIsHit = true;
                }
            }
            if (shipIsHit) {
                ship.shipIsHit(x,y);
                if (ship.shipIsSunk()) {
                    numberOfSunkenShips++;
                    if(numberOfSunkenShips == 5) {
                        return ShotType.ALLSUNK;
                    }
                    return ShotType.SUNK;
                }
                return ShotType.HIT;
            }
        }
        return  ShotType.MISSED;
    }

    public Ship removeShip(int x, int y) {
        Ship ship = checkForShip(x,y);
        if(ship != null) {
            allShips.remove(ship);
        }
        return ship;
    }

    public void removeAllShips() {
        allShips.clear();
    }

    public void setSquareState(int xPos, int yPos, SquareState state) {
        squares[xPos][yPos].setSquareState(state);
    }

    public Square[][] getSquares() {
        return squares;
    }


}
