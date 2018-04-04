package com.spiderbiggen.pmv.models;

import java.util.Locale;

public enum GameMap {
    ERANGEL("/img/maps/erangel_full_map_downscaled.jpg", 819_500.0), // 819_500;),
    MIRAMAR("/img/maps/miramar_full_map_downscaled.jpg", 819_500.0);

    private final String relativePath;
    private final double mapSize;

    GameMap(final String relativePath, final double scale) {
        this.relativePath = relativePath;
        this.mapSize = scale;
    }

    /**
     * Returns the map for the given string. Defaults to ERANGEL if the string doesn't represent any map.
     * @param string any string
     * @return map, default ERANGEL
     */
    public static GameMap fromString(final String string) {
        String name = string.toUpperCase(Locale.ENGLISH);
        try {
            switch (name) {
                case Constants.ERANGEL_MAIN:
                    return ERANGEL;
                case Constants.DESERT_MAIN:
                    return MIRAMAR;
                default:
                    return GameMap.valueOf(name);
            }
        } catch (IllegalArgumentException e) {
            return ERANGEL;
        }
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
     * Gets mapSize
     *
     * @return value of mapSize
     */
    public double getMapSize() {
        return mapSize;
    }

    /**
     * Gets map scale
     *
     * @return value of 1 / mapSize
     */
    public double getScale() {
        return 1d / mapSize;
    }

    private static class Constants {
        static final String ERANGEL_MAIN = "ERANGEL_MAIN";
        static final String DESERT_MAIN = "DESERT_MAIN";
    }
}
