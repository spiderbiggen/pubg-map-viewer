package com.spiderbiggen.pmv.models;

import com.spiderbiggen.pmv.models.telemetry.Point3D;

import java.util.Locale;

public enum GameMap {
    ERANGEL(819_500.0), // 819_500
    MIRAMAR(819_500.0);

    private final double mapSize;

    GameMap(final double scale) {
        this.mapSize = scale;
    }

    /**
     * Returns the map for the given string. Defaults to ERANGEL if the string doesn't represent any map.
     *
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

    //    "/img/maps/erangel_full_map.jpg", "/img/maps/erangel_minimap_lowres.jpg", "/img/maps/erangel_full_map_small.jpg"
    //    "/img/maps/miramar_full_map.jpg", "/img/maps/miramar_minimap_lowres.jpg", "/img/maps/miramar_full_map_small.jpg"

    /**
     * Gets Relative Path to the image file for this map.
     *
     * @return relative Path to image.
     */
    public String getHighRes() {
        return String.format(Constants.HIGH_RES_PATTERN, name().toLowerCase());
    }

    /**
     * Gets medRes
     *
     * @return value of medRes
     */
    public String getMedRes() {
        return String.format(Constants.MED_RES_PATTERN, name().toLowerCase());
    }

    /**
     * Gets lowRes
     *
     * @return value of lowRes
     */
    public String getLowRes() {
        return String.format(Constants.LOW_RES_PATTERN, name().toLowerCase());
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

    public Point3D scaleLocation(Point3D location) {
        return location.getScaled(getScale());
    }

    private static class Constants {
        static final String ERANGEL_MAIN = "ERANGEL_MAIN";
        static final String DESERT_MAIN = "DESERT_MAIN";
        static final String IMG_BASE_DIR = "/img/maps";
        static final String HIGH_RES_PATTERN = IMG_BASE_DIR + "/%s_full_map.jpg";
        static final String MED_RES_PATTERN = IMG_BASE_DIR + "/%s_minimap_lowres.jpg";
        static final String LOW_RES_PATTERN = IMG_BASE_DIR + "/%s_full_map_small.jpg";
    }
}
