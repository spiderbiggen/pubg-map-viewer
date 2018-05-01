package com.spiderbiggen.pmv.models.drawing;

import com.spiderbiggen.pmv.util.Colors;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Path2D {

    private static final double ALPHA = 0.85;

    private List<Point2D> points;
    private List<Point2D> scaledPoints;

    private double scale = 1;
    private double translateX;
    private double translateY;
    private Color color = Colors.getRandomColor(ALPHA);

    public Path2D(Collection<Point2D> points) {
        setPoints(points);
    }

    /**
     * Sets points.
     *
     * @param points the new value of points
     */
    public void setPoints(Collection<Point2D> points) {
        this.points = new ArrayList<>(points);
        setScale(scale);
    }

    /**
     * Sets scale.
     *
     * @param scale the new value of scale
     */
    public Path2D setScale(double scale) {
        this.scale = scale;
        scaledPoints = points.stream().map(p -> p.getCopy().scale(scale)).collect(Collectors.toList());
        double translateX = this.translateX;
        double translateY = this.translateY;
        this.translateX = 0;
        this.translateY = 0;
        translateTo(translateX, translateY);
        return this;
    }
    
    public Path2D translate(double dX, double dY) {
        translateX += dX;
        translateY += dY;
        scaledPoints.forEach(p -> p.translate(dX, dY));
        return this;
    }

    public Path2D translateTo(double x, double y) {
        double dX = x - translateX;
        double dY = y - translateY;
        return translate(dX, dY);
    }

    public void draw(GraphicsContext context) {
        if (scaledPoints.isEmpty()) return;
        context.setStroke(color);
        context.beginPath();
        Point2D start = scaledPoints.get(0);
        context.moveTo(start.getX(), start.getY());
        for (int i = 1; i < scaledPoints.size(); i++) {
            Point2D location = scaledPoints.get(i);
            context.lineTo(location.getX(), location.getY());
        }
        context.stroke();
    }

    public void randomizeColor() {
        color = Colors.getRandomColor(ALPHA);
    }

    /**
     * Gets color
     *
     * @return value of color
     */
    public Paint getColor() {
        return color;
    }
}
