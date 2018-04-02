package com.spiderbiggen.pmv.models;

import java.util.Locale;

public enum GameMap {
    ERANGEL("/img/maps/erangel_full_map_downscaled.jpg", 1.0),
    MIRAMAR("/img/maps/miramar_full_map_downscaled.jpg", 1.0);

    private final String relativePath;
    private final double scale;

    GameMap(final String relativePath, final double scale) {
        this.relativePath = relativePath;
        this.scale = scale;
    }

    public static GameMap fromString(final String string) {
        return GameMap.valueOf(string.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Gets Relative Path to the image file for this map.
     *
     * @return relative Path to image.
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * Gets scale
     *
     * @return value of scale
     */
    public double getScale() {
        return scale;
    }
}
