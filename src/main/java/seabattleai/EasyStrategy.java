package seabattleai;

import java.awt.*;

public class EasyStrategy extends Strategy{
    Point randomPoint = new Point();

    public Point fireShotOpponent() {
        return super.noShipHit(randomPoint);
    }
}
