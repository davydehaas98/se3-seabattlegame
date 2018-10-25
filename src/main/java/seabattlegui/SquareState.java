/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seabattlegui;

/**
 * Indicate the state of a square.
 * @author Nico Kuijpers
 */
public enum SquareState {
    WATER,        // No ship is positioned at this square                   BLUE
    SHIP,         // A ship is positioned at this square                    GREY
    SHOTMISSED,   // A shot was fired at this square, but no hit            WHITE
    SHOTHIT,      // A shot was fired at this square and a ship was hit     ORANGE
    SHIPSUNK;     // A shot was fired at this square and a ship is sunk     RED
}
