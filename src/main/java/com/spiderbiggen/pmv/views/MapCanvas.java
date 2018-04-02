package com.spiderbiggen.pmv.views;

import com.spiderbiggen.pmv.models.GameMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MapCanvas extends Canvas {

    private static final double MAX_SIZE = Double.MAX_VALUE;
    private static final double MIN_SIZE = 64;
    //log2(1)
    private static final double MIN_ZOOM = 0;
    //log2(10)
    private static final double MAX_ZOOM = 3.32192809489;

    //can't be 0
    private double drawSize = 1;
    private Image image = null;
    private double zoom = 0;
    private double x = 0, y = 0;

    /**
     * Sets map.
     *
     * @param map the new value of map
     */
    public void setMap(GameMap map) {
        this.image = new Image(getClass().getResourceAsStream(map.getRelativePath()));
        draw();
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double minWidth(double height) {
        return MIN_SIZE;
    }

    @Override
    public double minHeight(double width) {
        return MIN_SIZE;
    }

    @Override
    public double prefHeight(double width) {
        return minHeight(width);
    }

    @Override
    public double maxWidth(double height) {
        return MAX_SIZE;
    }

    @Override
    public double maxHeight(double width) {
        return MAX_SIZE;
    }

    @Override
    public void resize(final double width, final double height) {
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;

        super.setWidth(width);
        super.setHeight(height);

        changeZoom(0, centerX, centerY);
    }

    private void recalculateDrawSize() {
        final double width = getWidth();
        final double height = getHeight();
        final double zoom = Math.pow(2, this.zoom);
        drawSize = (width < height ? width : height) * zoom;
    }

    public void changeZoom(double dZoom) {
        changeZoom(dZoom, getWidth() / 2, getHeight() / 2);
    }

    public void changeZoom(double dZoom, double centerX, double centerY) {
        //between 0-1
        double imageX = (centerX - x) / drawSize;
        double imageY = (centerY - y) / drawSize;

        zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom + dZoom));

        recalculateDrawSize();
        setX(centerX - (imageX * drawSize));
        setY(centerY - (imageY * drawSize));
        draw();
    }

    public void move(double dX, double dY) {
        setX(x + dX);
        setY(y + dY);

        draw();
    }

    private void setX(final double newX) {
        double minX = getWidth() - drawSize;
        if (minX < 0) {
            x = Math.max(minX, Math.min(0, newX));
        } else {
            x = Math.max(0, Math.min(minX, newX));
        }
    }

    private void setY(final double newY) {
        double minY = getHeight() - drawSize;
        if (minY < 0) {
            y = Math.max(minY, Math.min(0, newY));
        } else {
            y = Math.max(0, Math.min(minY, newY));
        }
    }

    private void clear() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }

    private void draw() {
        if (image == null) return;
        clear();
        GraphicsContext context = getGraphicsContext2D();
        context.drawImage(image, x, y, drawSize, drawSize);
        System.out.println("(" + x + ", " + y + ")");
        System.out.println(drawSize);
    }

}
