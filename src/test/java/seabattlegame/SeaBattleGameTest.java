package seabattlegame;

import org.junit.Before;
import org.junit.Test;
import seabattlegui.ShipType;
import seabattlegui.ShotType;

import static org.junit.Assert.*;

public class SeaBattleGameTest{
    private ISeaBattleGame seaBattleGame;

    @Before
    public void setUp() {
        seaBattleGame = new SeaBattleGame();
    }

    @Test
    public void registerPlayer() {

    }

    @Test
    public void placeShip() {
        boolean result = seaBattleGame.placeShip(0, ShipType.AIRCRAFTCARRIER, 1, 1, true);
        assertTrue(result);
    }

    @Test
    public void placeShipAutomatically(){
        boolean result = seaBattleGame.placeShipsAutomatically(0);
        assertTrue(result);
    }

    @Test
    public void removeShip() {
        seaBattleGame.placeShip(0,ShipType.AIRCRAFTCARRIER,1,1,true);
        boolean result = seaBattleGame.removeShip(0, 1, 1);
        assertTrue(result);
    }

    @Test
    public void removeAllShips() {
        boolean result = seaBattleGame.removeAllShips(0);
        assertTrue(result);
    }

    @Test
    public void notifyWhenReady() {
        boolean result = seaBattleGame.notifyWhenReady(0);
        assertTrue(result);
    }

    @Test
    public void fireShotPlayer() {
        seaBattleGame.placeShip(0,ShipType.AIRCRAFTCARRIER,1,1,true);
        ShotType result = seaBattleGame.fireShotPlayer(0,1,1);
        assertEquals(ShotType.HIT,result);
    }

    @Test
    public void fireShotOpponent() {
        ShotType result = seaBattleGame.fireShotOpponent(1);
        assertEquals(ShotType.MISSED, result);
    }

    @Test
    public void startNewGame() {
        boolean result = seaBattleGame.startNewGame(0);
        assertTrue(result);
    }
}