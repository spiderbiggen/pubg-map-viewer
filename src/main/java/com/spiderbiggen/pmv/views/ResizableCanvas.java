package com.spiderbiggen.pmv.views;

import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas {

    private static final double MAX_SIZE = Double.MAX_VALUE;
    private static final double MIN_SIZE = 64;

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
}
