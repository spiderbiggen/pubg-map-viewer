package com.spiderbiggen.pmv.models.telemetry;

import com.spiderbiggen.pmv.models.GameMap;
import com.spiderbiggen.pmv.models.PubgUser;
import com.spiderbiggen.pmv.models.telemetry.objects.PubgCommon;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Telemetry {

    private final List<PubgGameEvent> events;
    private final SortedSet<PubgUser> players;
    private final Map<PubgUser, List<PubgGameEvent>> userEventMap;
    private final GameMap map;

    public Telemetry(final List<PubgGameEvent> events) {
        this.events = Collections.unmodifiableList(events);
        this.userEventMap = Collections.unmodifiableMap(getUserEventMap(this.events));
        this.players = Collections.unmodifiableSortedSet(new TreeSet<>(userEventMap.keySet()));
        this.map = getGameMapForTelemetry(this.events);
    }

    public static Telemetry parse(String json) {
        if (json == null) return null;
        return parse(new JSONTokener(json));
    }

    public static Telemetry parse(InputStream json) {
        if (json == null) return null;
        return parse(new JSONTokener(json));
    }

    public static Telemetry parse(JSONTokener json) {
        JSONArray object = new JSONArray(json);
        var gameEventList = StreamSupport.stream(object.spliterator(), false)
                .filter(f -> f instanceof JSONObject)
                .map(o -> (JSONObject) o)
                .map(PubgGameEvent::parse)
                .collect(Collectors.toUnmodifiableList());
        return new Telemetry(gameEventList);
    }

    private static GameMap getGameMapForTelemetry(List<PubgGameEvent> events) {
        return events.stream()
                .map(PubgGameEvent::getCommon)
                .filter(Objects::nonNull)
                .map(PubgCommon::getMapName)
                .filter(s -> !s.isEmpty())
                .findAny()
                .map(GameMap::fromString)
                .orElse(GameMap.ERANGEL);
    }

    private static Map<PubgUser, List<PubgGameEvent>> getUserEventMap(List<PubgGameEvent> events) {
        return events.stream()
                .filter(gameEvent -> gameEvent.getCharacter() != null)
                .filter(gameEvent -> gameEvent.getCharacter().getLocation() != null)
                .sorted()
                .collect(Collectors.groupingBy(PubgGameEvent::getCharacter));
    }

    /**
     * Gets events
     *
     * @return value of events
     */
    public List<PubgGameEvent> getEvents() {
        return events;
    }

    /**
     * Gets players
     *
     * @return value of players
     */
    public SortedSet<PubgUser> getPlayers() {
        return players;
    }

    /**
     * Gets userEventMap
     *
     * @return value of userEventMap
     */
    public Map<PubgUser, List<PubgGameEvent>> getUserEventMap() {
        return userEventMap;
    }

    /**
     * Gets map
     *
     * @return value of map
     */
    public GameMap getMap() {
        return map;
    }

}
