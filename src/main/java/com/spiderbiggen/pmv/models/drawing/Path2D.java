package com.spiderbiggen.pmv.models.drawing;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Path2D {
    private List<Point2D> points;
    private List<Point2D> scaledPoints;

    private double scale;
    private double translateX;
    private double translateY;

    public Path2D(Collection<? extends Point2D> points) {
        setPoints(points);
    }

    /**
     * Sets points.
     *
     * @param points the new value of points
     */
    public void setPoints(Collection<? extends Point2D> points) {
        this.points = new ArrayList<>(points);
        setScale(scale);
    }

    /**
     * Gets scaledPoints
     *
     * @return value of scaledPoints
     */
    public List<Point2D> getScaledPoints() {
        return scaledPoints;
    }

    /**
     * Gets scale
     *
     * @return value of scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * Sets scale.
     *
     * @param scale the new value of scale
     */
    public Path2D setScale(double scale) {
        this.scale = scale;
        scaledPoints = points.stream().map(p -> p.getScaled(scale)).collect(Collectors.toList());
        double translateX = this.translateX;
        double translateY = this.translateY;
        this.translateX = 0;
        this.translateY = 0;
        translateTo(translateX, translateY);
        return this;
    }

    /**
     * Gets translateX
     *
     * @return value of translateX
     */
    public double getTranslateX() {
        return translateX;
    }

    /**
     * Gets translateY
     *
     * @return value of translateY
     */
    public double getTranslateY() {
        return translateY;
    }

    public Path2D translate(double dX, double dY) {
        translateX += dX;
        translateY += dY;
        scaledPoints = scaledPoints.stream().map(p -> p.translate(dX, dY)).collect(Collectors.toList());
        return this;
    }

    public Path2D translateTo(double x, double y) {
        double dX = x - translateX;
        double dY = y - translateY;
        return translate(dX, dY);
    }

    public void draw(GraphicsContext context) {
        if (scaledPoints.isEmpty()) return;
        context.beginPath();
        Point2D start = scaledPoints.get(0);
        context.moveTo(start.getX(), start.getY());
        for (int i = 1; i < scaledPoints.size(); i++) {
            Point2D location = scaledPoints.get(i);
            context.lineTo(location.getX(), location.getY());
        }
        context.stroke();
    }

}
