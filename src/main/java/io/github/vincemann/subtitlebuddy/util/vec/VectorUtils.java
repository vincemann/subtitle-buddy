package io.github.vincemann.subtitlebuddy.util.vec;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VectorUtils {

    public static Vector2D getVecWithinBounds(Vector2D pos, Vector2D bounds, Vector2D nodeSize) {
        if (pos == null || !Vector2D.isVectorInBounds(bounds.getX(), bounds.getY(), 0, 0, pos)) {
            log.warn("pos null, using center");
            return getCenterPos(bounds,nodeSize);
        }
        else{
            return pos;
        }
    }

    private static Vector2D getCenterPos(Vector2D bounds, Vector2D nodeSize){
        return new Vector2D(bounds.getX()/2-nodeSize.getX()/2, bounds.getY()/2-nodeSize.getY()/2);
    }
}
