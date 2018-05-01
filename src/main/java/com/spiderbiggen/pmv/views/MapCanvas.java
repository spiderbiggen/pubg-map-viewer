package com.spiderbiggen.pmv.views;

import com.spiderbiggen.pmv.models.drawing.Path2D;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapCanvas extends ResizableCanvas {

    /**
     * 2log(1)
     */
    private static final double MIN_ZOOM = 0;

    /**
     * 2log(20)
     */
    private static final double MAX_ZOOM = 4.32192809489;

    //can't be 0
    private double drawSize = 1;
    private double zoom = 0;
    private double x = 0, y = 0;
    private Image image = null;

    private List<Path2D> userPaths = new ArrayList<>();

    /**
     * Sets backgroundImage.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Sets userPaths.
     *
     * @param locations the new value of userPaths
     */
    public void setUserPaths(Collection<Path2D> locations) {
        this.userPaths = new ArrayList<>(locations);
        userPaths.forEach(path2D -> path2D.setScale(drawSize).translateTo(x, y));
    }

    private void recalculateDrawSize() {
        final double width = getWidth();
        final double height = getHeight();
        final double zoom = Math.pow(2, this.zoom);
        drawSize = (width < height ? width : height) * zoom;
    }

    public void changeZoom(double dZoom, double centerX, double centerY) {
        //between 0-1
        double imageX = (centerX - x) / drawSize;
        double imageY = (centerY - y) / drawSize;

        zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom + dZoom));

        recalculateDrawSize();
        setX(centerX - (imageX * drawSize));
        setY(centerY - (imageY * drawSize));
        userPaths.forEach(path2D -> path2D.setScale(drawSize).translateTo(x, y));
    }

    public void move(double dX, double dY) {
        setX(x + dX);
        setY(y + dY);
        userPaths.forEach(path2D -> path2D.translateTo(x, y));
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

    private void clear(GraphicsContext context) {
        context.clearRect(0, 0, getWidth(), getHeight());
    }

    public void draw() {
        final var pointSize = Math.max(2, Math.pow(2, zoom) / 2);
        final var context2D = getGraphicsContext2D();
        context2D.setLineCap(StrokeLineCap.ROUND);
        context2D.setLineJoin(StrokeLineJoin.ROUND);
        context2D.setLineWidth(pointSize);

        Platform.runLater(() -> {
            clear(context2D);
            if (image != null) {
                context2D.drawImage(image, x, y, drawSize, drawSize);
            }
            if (userPaths == null || userPaths.isEmpty()) return;

            userPaths.forEach(path -> path.draw(context2D));
        });
    }

    @Override
    public void resize(final double width, final double height) {
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;

        super.setWidth(width);
        super.setHeight(height);

        changeZoom(0, centerX, centerY);
        draw();
    }
}
