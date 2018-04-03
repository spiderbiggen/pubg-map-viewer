package com.spiderbiggen.pmv.views;

import com.spiderbiggen.pmv.JsonParser;
import com.spiderbiggen.pmv.models.GameMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MapCanvas extends Canvas {

    private static final double MAX_SIZE = Double.MAX_VALUE;
    private static final double MIN_SIZE = 64;
    //log2(1)
    private static final double MIN_ZOOM = 0;
    //log2(10)
    private static final double MAX_ZOOM = 3.32192809489;
    private static final String[] colors = {"#f00", "#0f0", "#00f", "ff0", "f0f", "0ff"};

    //can't be 0
    private double drawSize = 1;
    private GameMap map;
    private Image image = null;
    private double zoom = 0;
    private double x = 0, y = 0;
    private Map<String, List<JsonParser.GameEvent>> locations;
    private Set<String> playerNames = new HashSet<>();


    /**
     * Sets map.
     *
     * @param map the new value of map
     */
    public void setMap(GameMap map) {
        this.map = map;
        this.image = new Image(getClass().getResourceAsStream(map.getRelativePath()));
        draw();
    }

    /**
     * Sets locations.
     *
     * @param locations the new value of locations
     */
    public void setPlayers(Map<String, List<JsonParser.GameEvent>> locations) {
        this.locations = locations;
    }

    /**
     * Sets playerNames.
     *
     * @param playerNames the new value of playerNames
     */
    public void setPlayerNames(String... playerNames) {
        this.playerNames = new HashSet<>(Arrays.asList(playerNames));
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

    public void draw() {
        if (image == null) return;
        clear();
        GraphicsContext context = getGraphicsContext2D();
        context.drawImage(image, x, y, drawSize, drawSize);
        context.setFill(Paint.valueOf("#f00"));
        context.setStroke(Paint.valueOf("#00f"));
        double mapSize = 819_550; // 819_500;
        double pointSize = Math.max(2, Math.pow(2, zoom) / 2);
        if (locations == null || locations.isEmpty()) return;
        AtomicInteger i = new AtomicInteger();
        locations.entrySet().stream()
                .filter(entry -> playerNames.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(events -> {
                    List<JsonParser.Location> normalized = events.stream()
                            .map(JsonParser.GameEvent::getCharacter)
                            .map(JsonParser.Character::getLocation)
                            .map(location -> location.getNormalized(mapSize))
                            .collect(Collectors.toList());
                    JsonParser.Location start = normalized.get(0);
                    context.beginPath();
                    context.setStroke(Paint.valueOf(colors[i.incrementAndGet() % colors.length]));
                    context.setLineWidth(pointSize);
                    context.moveTo(start.getX() * drawSize + x, start.getY() * drawSize + y);
                    normalized.listIterator(1).forEachRemaining(location -> context.lineTo(location.getX() * drawSize + x, location.getY() * drawSize + y));
                    context.stroke();
                });

    }


}
