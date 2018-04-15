package com.spiderbiggen.pmv.models.drawing;

public class Point2D {
    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets x
     *
     * @return value of x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y
     *
     * @return value of y
     */
    public double getY() {
        return y;
    }

    public Point2D getScaled(double scale) {
        return new Point2D(x * scale, y * scale);
    }

    public Point2D translate(double dX, double dY) {
        return new Point2D(x + dX, y + dY);
    }
}
