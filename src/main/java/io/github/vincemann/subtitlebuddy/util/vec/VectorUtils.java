package io.github.vincemann.subtitlebuddy.util.vec;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VectorUtils {

    public static boolean isVecWithinBounds(Vector2D pos, Vector2D bounds) {
        return Vector2D.isVectorInBounds(bounds.getX(), bounds.getY(), 0, 0, pos);
    }

    public static Vector2D getCenterPos(Vector2D bounds, Vector2D nodeSize){
        return new Vector2D(bounds.getX()/2-nodeSize.getX()/2, bounds.getY()/2-nodeSize.getY()/2);
    }
}
