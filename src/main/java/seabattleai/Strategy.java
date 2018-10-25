package seabattleai;

import seabattlegui.ShotType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Strategy implements IStrategy {
    private ArrayList<Point> shotsFired;
    private ArrayList<ShotType> shotsType;

    Strategy() {
        this.shotsFired = new ArrayList<>();
        this.shotsType = new ArrayList<>();
    }

    ArrayList<Point> getShotsFired() {
        return shotsFired;
    }

    ArrayList<ShotType> getShotsType() {
        return shotsType;
    }

    public void addShotType(ShotType shotType) {
        shotsType.add(shotType);
    }

    Point checkShotsFired(int x, int y) {
        boolean firedShotExists = false;
        for (Point firedShot : shotsFired) {
            if (firedShot.x == x && firedShot.y == y) {
                firedShotExists = true;
            }
        }
        if(firedShotExists){
            return fireShotOpponent();
        }
        return new Point(x, y);
    }

    public abstract Point fireShotOpponent();

    //No Ship has been hit or a Ship just sunk
    Point noShipHit(Point randomPoint) {
        randomPoint.x = new Random().nextInt(10);
        randomPoint.y = new Random().nextInt(10);
        Point shot = checkShotsFired(randomPoint.x, randomPoint.y);
        shotsFired.add(shot);
        return shot;
    }

    //A Ship has been hit
    Point shipHit(Point randomPoint) {
        Point lastShotHit = getShotsFired().get(getShotsType().lastIndexOf(ShotType.HIT));
        randomPoint.x = new Random().nextInt(3) + lastShotHit.x - 1;
        randomPoint.y = new Random().nextInt(3) + lastShotHit.y - 1;
        return checkShotsFired(randomPoint.x, randomPoint.y);
    }
}
