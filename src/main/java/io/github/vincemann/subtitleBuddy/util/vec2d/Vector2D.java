package io.github.vincemann.subtitleBuddy.util.vec2d;

import static com.google.common.base.Preconditions.checkArgument;


public class Vector2D {

    private static final char DELIMITER = ':';

    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(String vecString){
        checkArgument(vecString.length()>=3);
        Double x = null;
        Double y = null;
        try {
            StringBuilder s = new StringBuilder();

            for(int i = 0 ; i<vecString.length();i++){
                char c = vecString.charAt(i);
                if(c==DELIMITER){
                    x = Double.valueOf(s.toString());
                    s.setLength(0);
                }else {
                    s.append(c);
                }
            }
            if(x==null){
                throw new VectorDecodeException("no x value for vector found");
            }
            y = Double.valueOf(s.toString());
        }catch (NumberFormatException e){
            throw new VectorDecodeException(e);
        }
        this.x=x;
        this.y=y;
    }

    public String toString(){
        return getX()+""+DELIMITER+getY();
    }

    public static boolean isVectorInBounds(double maxX, double maxY, double minX, double minY, Vector2D vector2D){
        return
                vector2D.getX()<=maxX && vector2D.getX()>=minX
                         &&
                vector2D.getY()<=maxY && vector2D.getY()>=minY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
