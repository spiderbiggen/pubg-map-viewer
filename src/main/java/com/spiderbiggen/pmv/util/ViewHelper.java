package com.spiderbiggen.pmv.util;

import javafx.scene.paint.Color;

import java.util.Random;

public class ViewHelper {

    private static Random random = new Random();

    public static Color getRandomColor() {
        return getRandomColor(1.0);
    }

    public static Color getRandomColor(double alpha) {
        return Color.hsb(360 * random.nextDouble(), 1, 1, alpha);
    }

    /**
     * Sets random.
     *
     * @param random the new value of random
     */
    public static void setRandom(Random random) {
        ViewHelper.random = random;
    }
}
