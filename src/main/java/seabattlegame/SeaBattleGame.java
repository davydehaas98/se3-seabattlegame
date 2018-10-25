/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package seabattlegame;

import seabattlegui.ISeaBattleGUI;
import seabattlegui.ShipType;
import seabattlegui.ShotType;
import seabattlegui.SquareState;
import seabattleai.*;

import java.awt.*;
import java.util.Random;

/*
 The Sea Battle game. To be implemented.
 @author Nico Kuijpers
 */
public class SeaBattleGame implements ISeaBattleGame {

    private ISeaBattleGUI application;
    boolean singlePlayerMode;
    private IStrategy strategy;
    private Player[] players;
    private ICommunicator communicator;
    private static SeaBattleGame game;


    @Override
    public int registerPlayer(String name, ISeaBattleGUI application, boolean singlePlayerMode) {
        this.application = application;
        this.singlePlayerMode = singlePlayerMode;
        connectToServer();
        players = new Player[2];
        players[0] = new Player();
        players[0].setName(name);
        if(singlePlayerMode) {
            players[1] = new Player();
        }
        else {
            //players[1] = getOpponent from server;
        }
        if (singlePlayerMode) {
            this.strategy = new EasyStrategy();
            placeShipsAutomatically(1);
        }
        return 0;
    }

    @Override
    public boolean placeShipsAutomatically(int playerNr) {
        removeAllShips(playerNr);
        // Try to place the Aircraft carrier
        placeShipAutomatically(playerNr, ShipType.AIRCRAFTCARRIER);
        // Try to place the Battleship
        placeShipAutomatically(playerNr, ShipType.BATTLESHIP);
        // Try to place the Cruiser
        placeShipAutomatically(playerNr, ShipType.CRUISER);
        // Try to place the Submarine
        placeShipAutomatically(playerNr, ShipType.SUBMARINE);
        // Try to place the Minesweeper
        placeShipAutomatically(playerNr, ShipType.MINESWEEPER);
        return true;
    }

    private void placeShipAutomatically(int playerNr, ShipType shipType) {
        Random rdm = new Random();
        int randomX = rdm.nextInt(10);
        int randomY = rdm.nextInt(10);
        boolean randomOrientation = rdm.nextBoolean();
        if(!shipTypeExists(playerNr, shipType)) {
            // Ship of this type does not exist so try to place ship
            if (!placeShip(playerNr, shipType, randomX, randomY, randomOrientation)) {
                // Ship placement failed, try again with new coords
                placeShipAutomatically(playerNr, shipType);
            }
        }
    }

    @Override
    public boolean placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        Player player = players[playerNr];
        Ship ship = new Ship(shipType);
        // Check if there is already a ship of this type
        if (player.getGrid().checkShipTypePlaced(shipType)) {
            return false;
        }
        // Check if the ship fits on the grid
        if(!shipFitsOnGrid(shipType, bowX, bowY, horizontal)) {
            return false;
        }
        // Check if one of the squares is already occupied by another ship
        if (shipOccupiesSpace(playerNr, shipType, bowX, bowY, horizontal)) {
            return false;
        }
        // Ship is able to be placed
        player.getGrid().addShip(ship, bowX, bowY, horizontal);
        // Set the correct square color on the GUI
        for (int i = 0; i < ship.getShipLength() && playerNr == 0; i++) {
            if (horizontal) {
                application.showSquarePlayer(playerNr, bowX + i, bowY, SquareState.SHIP);
            } else {
                application.showSquarePlayer(playerNr, bowX, bowY + i, SquareState.SHIP);
            }
        }
        // DEBUG CONSOLE OUTPUT
        //printDebugArray(player);
        return true;
    }

    private boolean shipTypeExists(int playerNr, ShipType shipType) {
        Player player = players[playerNr];
        // Check if there is already a ship of this type
        return player.getGrid().checkShipTypePlaced(shipType);
    }

    private boolean shipFitsOnGrid(ShipType shipType, int bowX, int bowY, boolean horizontal) {
        Ship ship = new Ship(shipType);
        // Check if the ship will fit on the grid
        if (horizontal) {
            // -1 is added to the equation to compensate for the fact that the selected square is
            // also part of the ship, and therefore you only have the add the remaining length to
            // reach the far end of the ship
            if (bowX + ship.getShipLength() - 1 > 9) {
                return false;
            }
        } else {
            // -1 is added to the equation to compensate for the fact that the selected square is
            // also part of the ship, and therefore you only have the add the remaining length to
            // reach the far end of the ship
            if (bowY + ship.getShipLength() - 1 > 9) {
                return false;
            }
        }
        return true;
    }

    private boolean shipOccupiesSpace(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        Player player = players[playerNr];
        Ship ship = new Ship(shipType);
        // Check if one of the squares is already occupied by another ship

        for (int i = 0; i < ship.getShipLength(); i++) {
            if (horizontal) {
                if (player.getGrid().checkForShip(bowX + i, bowY) != null) {
                    return true;
                }
            } else {
                if (player.getGrid().checkForShip(bowX, bowY + i) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeShip(int playerNr, int posX, int posY) {
        Ship removedShip = players[playerNr].getGrid().removeShip(posX, posY);
        // Check if a ship has been removed
        if (removedShip == null) {
            return false;
        }
        // Update GUI
        int x = removedShip.getBowPos().x;
        int y = removedShip.getBowPos().y;
        if (removedShip.isHorizontal()) {
            for (int i = 0; i < removedShip.getShipLength(); i++) {

                application.showSquarePlayer(playerNr, x + i, y, SquareState.WATER);
            }
        } else {
            for (int i = 0; i < removedShip.getShipLength(); i++) {
                application.showSquarePlayer(playerNr, x, y + i, SquareState.WATER);
            }
        }

        return true;
    }

    @Override
    public boolean removeAllShips(int playerNr) {
        players[playerNr].getGrid().removeAllShips();
        // Update GUI
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(playerNr == 0) {
                    application.showSquarePlayer(playerNr, i, j, SquareState.WATER);
                }
                else {
                    application.showSquareOpponent(1- playerNr, i , j, SquareState.WATER);
                }
            }
        }
        return true;
    }

    @Override
    public boolean notifyWhenReady(int playerNr) {
        return players[playerNr].getGrid().getAllShips().size() == 5;
    }

    @Override
    public ShotType fireShotPlayer(int playerNr, int posX, int posY) {
        Grid playerGrid = players[1 - playerNr].getGrid();
        ShotType shotType = playerGrid.checkShotState(posX, posY);
        switch (shotType) {
            case MISSED:
                playerGrid.setSquareState(posX, posY, SquareState.SHOTMISSED);
                application.showSquareOpponent(playerNr, posX, posY, SquareState.SHOTMISSED);
                break;
            case HIT:
                playerGrid.setSquareState(posX, posY, SquareState.SHOTHIT);
                application.showSquareOpponent(playerNr, posX, posY, SquareState.SHOTHIT);
                break;
            case SUNK:
                // continue in ALLSUNK. this way, both cases set the squares to sunk
            case ALLSUNK:
                Ship ship = playerGrid.checkForShip(posX, posY);
                for (int i = 0; i < ship.getShipLength(); i++) {
                    if (ship.isHorizontal()) {
                        playerGrid.setSquareState(ship.getBowPos().x + i, ship.getBowPos().y, SquareState.SHIPSUNK);
                        application.showSquareOpponent(playerNr, ship.getBowPos().x + i, ship.getBowPos().y, SquareState.SHIPSUNK);
                    } else {
                        playerGrid.setSquareState(ship.getBowPos().x, ship.getBowPos().y + i, SquareState.SHIPSUNK);
                        application.showSquareOpponent(playerNr, ship.getBowPos().x, ship.getBowPos().y + i, SquareState.SHIPSUNK);
                    }
                }
                break;
        }
        // DEBUG CONSOLE OUTPUT
        //printDebugArray(players[1- playerNr]);
        return shotType;
    }

    @Override
    public ShotType fireShotOpponent(int playerNr) {
        Point shotPoint = strategy.fireShotOpponent();
        Grid playerGrid = players[playerNr].getGrid();
        ShotType shotType = playerGrid.checkShotState(shotPoint.x, shotPoint.y);
        switch (shotType) {
            case MISSED:
                playerGrid.setSquareState(shotPoint.x, shotPoint.y, SquareState.SHOTMISSED);
                application.showSquarePlayer(playerNr, shotPoint.x, shotPoint.y, SquareState.SHOTMISSED);
                break;
            case HIT:
                playerGrid.setSquareState(shotPoint.x, shotPoint.y, SquareState.SHOTHIT);
                application.showSquarePlayer(playerNr, shotPoint.x, shotPoint.y, SquareState.SHOTHIT);
                break;
            case SUNK:
                Ship ship = playerGrid.checkForShip(shotPoint.x, shotPoint.y);
                for (int i = 0; i < ship.getShipLength(); i++) {
                    if (ship.isHorizontal()) {
                        playerGrid.setSquareState(ship.getBowPos().x + i, ship.getBowPos().y, SquareState.SHIPSUNK);
                        application.showSquarePlayer(playerNr, ship.getBowPos().x + i, ship.getBowPos().y, SquareState.SHIPSUNK);
                    } else {
                        playerGrid.setSquareState(ship.getBowPos().x, ship.getBowPos().y + i, SquareState.SHIPSUNK);
                        application.showSquarePlayer(playerNr, ship.getBowPos().x, ship.getBowPos().y + i, SquareState.SHIPSUNK);
                    }
                }
                break;
        }
        strategy.addShotType(shotType);
        application.opponentFiresShot(playerNr, shotType);
        // DEBUG CONSOLE OUTPUT
        //printDebugArray(players[playerNr]);
        return shotType;
    }

    private void printDebugArray(Player player) {
        System.out.println(player.getName() + "'s Grid");
        for(int i = 0; i<10; i++) {
            for(int j = 0; j<10; j++) {
                String str = player.getGrid().getSquares()[j][i].toString();
                System.out.print("[" + str.substring(0,5)  + "] ");
            }
            System.out.print("\n");
        }
        System.out.println("---------------------------------------------------------------------");
    }

    @Override
    public boolean startNewGame(int playerNr) {
        removeAllShips(playerNr);
        removeAllShips(1 - playerNr);
        players = null;
        return true;
    }

    private void connectToServer() {
        // Create the client web socket to communicate with other white boards
        communicator = SeaBattleClientSocket.getInstance();
        communicator.setGame(this);

        // Establish connection with server
        communicator.start();
    }

    public  static SeaBattleGame getInstance() {
        if(game == null)
        {
            game = new SeaBattleGame();
        }
        return game;
    }


    public void stopGame() {
        if (communicator != null) {
            communicator.stop();
        }
    }
}