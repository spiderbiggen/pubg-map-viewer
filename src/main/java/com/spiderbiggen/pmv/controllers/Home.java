package com.spiderbiggen.pmv.controllers;

import com.spiderbiggen.pmv.models.GameMap;
import com.spiderbiggen.pmv.models.PubgUser;
import com.spiderbiggen.pmv.models.drawing.Path2D;
import com.spiderbiggen.pmv.models.telemetry.PubgCharacter;
import com.spiderbiggen.pmv.models.telemetry.PubgGameEvent;
import com.spiderbiggen.pmv.models.telemetry.PubgLocation;
import com.spiderbiggen.pmv.models.telemetry.Telemetry;
import com.spiderbiggen.pmv.util.Images;
import com.spiderbiggen.pmv.views.MapCanvas;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class Home implements Initializable {

    @FXML
    private ListView<PubgUser> playerList;
    @FXML
    private MapCanvas canvas;
    private double startX, startY;
    private GameMap current = null;

    private static Map<PubgUser, List<PubgGameEvent>> removePreGamePositions(Map<PubgUser, List<PubgGameEvent>> players) {
        for (var user : players.keySet()) {
            var remove = new ArrayList<PubgGameEvent>();
            var gameEvents = players.get(user);
            for (var event : gameEvents) {
                if (event.getCommon().getIsGame() <= 0) {
                    remove.add(event);
                } else {
                    break;
                }
            }
            gameEvents.removeAll(remove);
        }
        return players;
    }

    private static Map<PubgUser, Path2D> normalizePositions(GameMap gameMap, Map<PubgUser, List<PubgGameEvent>> eventsForAccountId) {
        var map = new HashMap<PubgUser, Path2D>();
        for (Map.Entry<PubgUser, List<PubgGameEvent>> entry : eventsForAccountId.entrySet()) {
            var locations = entry.getValue().stream()
                    .map(PubgGameEvent::getCharacter)
                    .map(PubgCharacter::getLocation)
                    .map(PubgLocation::getPoint2D)
                    .map(point2D -> point2D.scale(gameMap.getScale()))
                    .collect(Collectors.toList());
            map.put(entry.getKey(), new Path2D(locations));
        }
        return map;
    }

    private void loadDataFromJson(File file) {
        var future = CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = new FileInputStream(file)) {
                return Telemetry.parse(inputStream);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
        future.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
        future.thenAccept(telemetry -> setImagesForMap(telemetry.getMap()));
        future.thenAcceptAsync(telemetry -> Platform.runLater(() -> {
            playerList.setItems(FXCollections.observableArrayList(telemetry.getPlayers()));
            playerList.getSelectionModel().selectFirst();
            canvas.setSelectedPlayers(playerList.getSelectionModel().getSelectedItems());
            canvas.draw();
        }));

        var locationFuture = future.thenApplyAsync((Telemetry players) -> removePreGamePositions(players.getUserEventMap()));
        locationFuture.exceptionally(e -> {
            e.printStackTrace();
            return null;
        });

        locationFuture.thenAcceptBoth(future, (pubgUserListMap, telemetry) -> {
            Map<PubgUser, Path2D> mapUserLocations = normalizePositions(telemetry.getMap(), pubgUserListMap);
            canvas.setUserPaths(mapUserLocations);
            canvas.randomizeColors();
        });
    }

    private void reload() {
        if (canvas != null && playerList != null) {
            canvas.setSelectedPlayers(playerList.getSelectionModel().getSelectedItems());
            canvas.draw();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerList.getSelectionModel().selectedItemProperty().addListener(observable -> reload());
        Platform.runLater(() -> selectFileDialog(null));
    }

    public void zoom(ScrollEvent scrollEvent) {
        double dZoom = Math.copySign(scrollEvent.isControlDown() ? 0.15D : 0.075D, scrollEvent.getDeltaY());
        double x = scrollEvent.getX();
        double y = scrollEvent.getY();
        canvas.changeZoom(dZoom, x, y);
        canvas.draw();
    }

    public void onDrag(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            double screenX = mouseEvent.getScreenX();
            double screenY = mouseEvent.getScreenY();
            double dX = screenX - startX;
            double dY = screenY - startY;
            canvas.move(dX, dY);
            canvas.draw();
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

    public void randomizeColors(ActionEvent actionEvent) {
        canvas.randomizeColors();
        reload();
    }

    public void refresh(ActionEvent actionEvent) {
        reload();
    }

    public void selectFileDialog(ActionEvent actionEvent) {
        var fileChooser = new FileChooser();
        var initialDirectory = new File("Telemetry");
        if (initialDirectory.exists() || initialDirectory.mkdirs()) {
            fileChooser.setInitialDirectory(initialDirectory);
            fileChooser.setTitle("Open Telemetry File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json (*.json)", "*.json"));
            File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
            if (file == null) return;
            loadDataFromJson(file);
        }
    }

    private void setImagesForMap(GameMap map) {
        if (current == map) return;
        current = map;
        Images.loadInternalAsync(map.getMedRes(), this::exceptionHandler)
                .thenAccept(this::acceptImage);
        Images.loadInternalAsync(map.getHighRes(), this::exceptionHandler)
                .thenAccept(this::acceptImage);
        canvas.setImage(Images.loadInternal(map.getLowRes(), this::exceptionHandler));
    }

    private void acceptImage(Image image) {
        Platform.runLater(() -> {
            canvas.setImage(image);
            canvas.draw();
        });
    }

    private void exceptionHandler(Throwable t) {
        t.printStackTrace();
    }
}
