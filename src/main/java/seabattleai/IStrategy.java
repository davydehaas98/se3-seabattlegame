package seabattleai;

import seabattlegui.ShotType;
import java.awt.*;

public interface IStrategy {
    Point fireShotOpponent();
    void addShotType(ShotType shotType);
}
