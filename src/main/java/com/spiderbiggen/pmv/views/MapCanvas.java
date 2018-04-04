package com.spiderbiggen.pmv.views;

import com.spiderbiggen.pmv.models.telemetry.PubgLocation;
import com.spiderbiggen.pmv.models.PubgUser;
import com.spiderbiggen.pmv.util.ViewHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapCanvas extends Canvas {

    private static final double MAX_SIZE = Double.MAX_VALUE;
    private static final double MIN_SIZE = 64;
    //log2(1)
    private static final double MIN_ZOOM = 0;
    //log2(20)
    private static final double MAX_ZOOM = 4.32192809489;

    //can't be 0
    private double drawSize = 1;
    private Image image = null;
    private double zoom = 0;
    private double x = 0, y = 0;
    private Map<PubgUser, List<PubgLocation>> mapAccountIdEvents;
    private Set<PubgUser> selectedPlayers = new HashSet<>();
    private Map<PubgUser, Color> colorMap;


    /**
     * Sets map.
     *
     * @param image the new image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Sets mapAccountIdEvents.
     *
     * @param locations the new value of mapAccountIdEvents
     */
    public void setEventsForAccountIds(Map<PubgUser, List<PubgLocation>> locations) {
        this.mapAccountIdEvents = new HashMap<>(locations);
    }

    /**
     * Sets selectedPlayers.
     *
     * @param selectedPlayers the new value of selectedPlayers
     */
    public void setSelectedPlayers(Collection<PubgUser> selectedPlayers) {
        this.selectedPlayers = new HashSet<>(selectedPlayers);
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
    }

    public void move(double dX, double dY) {
        setX(x + dX);
        setY(y + dY);
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

    public void draw() {
        if (image == null) return;
        clear();
        GraphicsContext context = getGraphicsContext2D();
        context.drawImage(image, x, y, drawSize, drawSize);
        if (mapAccountIdEvents == null || mapAccountIdEvents.isEmpty()) return;
        double pointSize = Math.max(2, Math.pow(2, zoom) / 2);
        context.setLineCap(StrokeLineCap.ROUND);
        context.setLineJoin(StrokeLineJoin.ROUND);
        context.setLineWidth(pointSize);
        mapAccountIdEvents.entrySet().stream()
                .filter(entry -> selectedPlayers.contains(entry.getKey()))
                .forEach(events -> {
                    List<PubgLocation> locations = events.getValue();
                    context.beginPath();
                    context.setStroke(colorMap.get(events.getKey()));
                    PubgLocation start = locations.get(0);
                    context.moveTo(start.getX() * drawSize + x, start.getY() * drawSize + y);
                    locations.listIterator(1).forEachRemaining(location -> context.lineTo(location.getX() * drawSize + x, location.getY() * drawSize + y));
                    context.stroke();
                });

    }

    public void randomizeColors() {
        colorMap = new HashMap<>();
        mapAccountIdEvents.forEach((player, g) -> colorMap.put(player, ViewHelper.getRandomColor(0.7)));
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
        draw();
    }
}
