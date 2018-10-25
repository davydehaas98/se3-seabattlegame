package seabattleai;

import seabattlegui.ShotType;

import java.awt.*;

public class NormalStrategy extends Strategy {
    public Point fireShotOpponent() {
        Point randomPoint = new Point();
        int lastHit = getShotsType().lastIndexOf(ShotType.HIT);
        int lastSunk = getShotsType().lastIndexOf(ShotType.SUNK);

        //A Ship has been hit
        if (lastHit > lastSunk) {
            return super.shipHit(randomPoint);
        }

        //No Ship has been hit or a Ship just sunk
        return super.noShipHit(randomPoint);
    }
}