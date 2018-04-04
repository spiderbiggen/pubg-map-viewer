package com.spiderbiggen.pmv.models.telemetry;

import org.json.JSONObject;

public class PubgLocation {

    private final double x;
    private final double y;
    private final double z;

    public PubgLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static PubgLocation parse(JSONObject object) {
        double x = object.getDouble("x");
        double y = object.getDouble("y");
        double z = object.getDouble("z");
        return new PubgLocation(x, y, z);
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

    /**
     * Gets z
     *
     * @return value of z
     */
    public double getZ() {
        return z;
    }


    public PubgLocation scale(double scale) {
        return new PubgLocation(getX() * scale, getY() * scale, getZ() * scale);
    }
}
