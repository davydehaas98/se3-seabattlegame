package seabattleai;

import seabattlegui.ShotType;

import java.awt.*;
import java.util.Random;

public class HardStrategy extends Strategy {
    public Point fireShotOpponent() {
        Point randomPoint = new Point();
        int lastSunk = getShotsType().lastIndexOf(ShotType.SUNK);
        int lastHit = getShotsType().lastIndexOf(ShotType.HIT);
        int secondLastHit = getShotsType().subList(lastSunk, lastHit).lastIndexOf(ShotType.HIT);

        //The x-axis or y-axis of the Ship is known
        if (lastSunk < secondLastHit) {
            Point lastShotHit = getShotsFired().get(lastHit);
            Point secondLastShotHit = getShotsFired().get(secondLastHit);

            //x-axis is known
            if (lastShotHit.x == secondLastShotHit.x) {
                randomPoint.y = new Random().nextInt(3) + lastShotHit.x - 1;
                randomPoint.x = lastShotHit.x;
            }

            //y-axis is known
            else {
                randomPoint.x = new Random().nextInt(3) + lastShotHit.x - 1;
                randomPoint.y = lastShotHit.y;
            }
            return checkShotsFired(randomPoint.x, randomPoint.y);
        }

        //A Ship has been hit
        else if (lastHit > lastSunk) {
            return super.shipHit(randomPoint);
        }

        //No Ship has been hit or a Ship just sunk
        return super.noShipHit(randomPoint);
    }
}