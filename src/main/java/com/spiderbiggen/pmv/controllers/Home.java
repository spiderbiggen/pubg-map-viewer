package com.spiderbiggen.pmv.controllers;

import com.spiderbiggen.pmv.models.GameMap;
import com.spiderbiggen.pmv.views.MapCanvas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private TextField userNameField;
    @FXML
    private MapCanvas canvas;
    private double startX, startY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.setMap(GameMap.ERANGEL);
    }

    public void selectedErangel(ActionEvent actionEvent) {
        canvas.setMap(GameMap.ERANGEL);
    }

    public void selectedMiramar(ActionEvent actionEvent) {
        canvas.setMap(GameMap.MIRAMAR);
    }

    public void zoom(ScrollEvent scrollEvent) {
        double dZoom = Math.copySign(scrollEvent.isControlDown() ? 0.15D : 0.075D, scrollEvent.getDeltaY());
        double x = scrollEvent.getX();
        double y = scrollEvent.getY();
        canvas.changeZoom(dZoom, x, y);
    }

    public void onDrag(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            double screenX = mouseEvent.getScreenX();
            double screenY = mouseEvent.getScreenY();
            double dX = screenX - startX;
            double dY = screenY - startY;
            canvas.move(dX, dY);
            startX = screenX;
            startY = screenY;
        }
    }

    public void onStartDrag(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            //Reset startPosition
            startX = mouseEvent.getScreenX();
            startY = mouseEvent.getScreenY();
        }
    }
}
