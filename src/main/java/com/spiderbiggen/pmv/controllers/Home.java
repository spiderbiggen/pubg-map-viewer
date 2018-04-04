package com.spiderbiggen.pmv.controllers;

import com.spiderbiggen.pmv.models.GameMap;
import com.spiderbiggen.pmv.models.PubgUser;
import com.spiderbiggen.pmv.models.telemetry.PubgCharacter;
import com.spiderbiggen.pmv.models.telemetry.PubgCommon;
import com.spiderbiggen.pmv.models.telemetry.PubgGameEvent;
import com.spiderbiggen.pmv.models.telemetry.PubgLocation;
import com.spiderbiggen.pmv.views.MapCanvas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
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
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.spiderbiggen.pmv.util.JsonParser.parse;
import static com.spiderbiggen.pmv.util.JsonParser.readJson;

public class Home implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private ListView<PubgUser> playerList;
    @FXML
    private MapCanvas canvas;
    private GameMap map = GameMap.ERANGEL;
    private double startX, startY;

    private static void removePreGamePositions(Map<PubgUser, List<PubgGameEvent>> players) {
        List<PubgUser> removeKeys = new ArrayList<>();
        for (PubgUser s : players.keySet()) {
            List<PubgGameEvent> remove = new ArrayList<>();
            List<PubgGameEvent> gameEvents = players.get(s);
            for (PubgGameEvent event : gameEvents) {
                if (event.getCommon().getIsGame() == 0) {
                    remove.add(event);
                } else {
                    break;
                }
            }
            gameEvents.removeAll(remove);
            if (gameEvents.isEmpty()) {
                removeKeys.add(s);
            }
        }
        removeKeys.forEach(players::remove);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (InputStream resourceAsStream = getClass().getResourceAsStream("/pc-telemetry.json")) {
            loadDataFromJson(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromJson(InputStream resourceAsStream) throws IOException {
        String json = readJson(resourceAsStream);
        List<PubgGameEvent> events = parse(json);
        if (events == null || events.isEmpty()) return;

        events.stream()
                .map(PubgGameEvent::getCommon)
                .filter(Objects::nonNull)
                .map(PubgCommon::getMapName)
                .filter(s -> !s.isEmpty())
                .findAny()
                .ifPresent(s -> map = GameMap.fromString(s));

        Map<PubgUser, List<PubgGameEvent>> eventsForAccountId = events.stream()
                .filter(gameEvent -> gameEvent.getCharacter() != null)
                .filter(gameEvent -> gameEvent.getCharacter().getLocation() != null)
                .sorted()
                .collect(Collectors.groupingBy(PubgGameEvent::getCharacter));

        removePreGamePositions(eventsForAccountId);
        Map<PubgUser, List<PubgLocation>> mapUserLocations = normalizePositions(eventsForAccountId);

        canvas.setEventsForAccountIds(mapUserLocations);
        canvas.randomizeColors();
        playerList.setItems(FXCollections.observableArrayList(mapUserLocations.keySet()));
        playerList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playerList.getSelectionModel().select(0);
        canvas.setSelectedPlayers(playerList.getSelectionModel().getSelectedItems());
        canvas.setImage(loadImage());
        canvas.draw();
        playerList.getSelectionModel().selectedItemProperty().addListener(observable -> reload());
    }

    private Image loadImage() {
        return new Image(getClass().getResourceAsStream(map.getRelativePath()));
    }

    private Map<PubgUser, List<PubgLocation>> normalizePositions(Map<PubgUser, List<PubgGameEvent>> eventsForAccountId) {
        Map<PubgUser, List<PubgLocation>> map = new HashMap<>();
        List<PubgLocation> locations;
        for (Map.Entry<PubgUser, List<PubgGameEvent>> entry : eventsForAccountId.entrySet()) {
            locations = entry.getValue().stream()
                    .map(PubgGameEvent::getCharacter)
                    .map(PubgCharacter::getLocation)
                    .map(this::scaleLocation)
                    .collect(Collectors.toList());
            map.put(entry.getKey(), locations);
        }
        return map;
    }

    private PubgLocation scaleLocation(PubgLocation location) {
        return location.scale(map.getScale());
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
            startX = screenX;
            startY = screenY;
            canvas.draw();
        }
    }

    public void onStartDrag(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            //Reset startPosition
            startX = mouseEvent.getScreenX();
            startY = mouseEvent.getScreenY();
        }
    }

    public void selectedErangel(ActionEvent actionEvent) {
        map = GameMap.ERANGEL;
        canvas.setImage(loadImage());
        canvas.draw();
    }

    public void selectedMiramar(ActionEvent actionEvent) {
        map = GameMap.MIRAMAR;
        canvas.setImage(loadImage());
        canvas.draw();
    }

    public void randomizeColors(ActionEvent actionEvent) {
        canvas.randomizeColors();
        canvas.draw();
    }

    public void refresh(ActionEvent actionEvent) {
        reload();
    }

    private void reload() {
        canvas.setSelectedPlayers(playerList.getSelectionModel().getSelectedItems());
        canvas.draw();
    }

    public void selectFileDialog(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File("Telemetry");
        try {
            if (initialDirectory.exists() || initialDirectory.createNewFile()) {
                fileChooser.setInitialDirectory(initialDirectory);
                fileChooser.setTitle("Open Telemetry File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json (*.json)", "*.json"));
                File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
                if (file == null) return;
                try (InputStream inputStream = new FileInputStream(file)) {
                    loadDataFromJson(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
