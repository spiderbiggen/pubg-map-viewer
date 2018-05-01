package com.spiderbiggen.pmv.models.drawing;

import com.spiderbiggen.pmv.models.CopyAble;

public class Point2D implements CopyAble<Point2D> {
    protected double x;
    protected double y;

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

    public Point2D scale(double scale) {
        x *= scale;
        y *= scale;
        return this;
    }

    public Point2D translate(double dX, double dY) {
        x += dX;
        y += dY;
        return this;
    }

    @Override
    public Point2D getCopy() {
        return new Point2D(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
