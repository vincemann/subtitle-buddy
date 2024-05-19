package io.github.vincemann.subtitlebuddy.util.vec;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VectorUtils {

    public static Vector2D getVecWithinBounds(Vector2D pos, Vector2D bounds) {
        if (pos == null) {
            log.warn("pos null, using center");
            return new Vector2D(bounds.getX() / 2, bounds.getY() / 2);
        }
        if (!Vector2D.isVectorInBounds(bounds.getX(), bounds.getY(), 0, 0, pos)) {
            log.warn("pos not within bounds, using center");
            return new Vector2D(bounds.getX() / 2, bounds.getY() / 2);
        }
        return pos;
    }
}
