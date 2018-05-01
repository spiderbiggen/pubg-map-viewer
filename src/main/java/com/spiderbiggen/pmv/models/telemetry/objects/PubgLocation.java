package com.spiderbiggen.pmv.models.telemetry.objects;

import com.spiderbiggen.pmv.models.drawing.Point2D;
import org.json.JSONObject;

public class PubgLocation extends Point2D {

    protected double z;

    public PubgLocation(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public static PubgLocation parse(JSONObject object) {
        double x = object.getDouble("x");
        double y = object.getDouble("y");
        double z = object.getDouble("z");
        return new PubgLocation(x, y, z);
    }

    /**
     * Gets z
     *
     * @return value of z
     */
    public double getZ() {
        return z;
    }

    @Override
    public PubgLocation scale(double scale) {
        super.scale(scale);
        z *= scale;
        return this;
    }

    @Override
    public PubgLocation getCopy() {
        return new PubgLocation(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * @param dX
     * @param dY
     * @param dZ
     * @return
     */
    public PubgLocation translate(double dX, double dY, double dZ) {
        super.translate(dX, dY);
        z += dZ;
        return this;
    }

    public Point2D getPoint2D() {
        return super.getCopy();
    }
}
