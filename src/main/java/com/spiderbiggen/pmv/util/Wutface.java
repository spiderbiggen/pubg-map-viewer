package com.spiderbiggen.pmv.util;

import com.spiderbiggen.pmv.models.telemetry.PubgGameEvent;
import com.spiderbiggen.pmv.models.telemetry.PubgLocation;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class Wutface {

    public static void main(String... args) throws IOException {
        StringBuilder json = new StringBuilder();
        try (InputStream inputStream = Wutface.class.getResourceAsStream("/pc-telemetry.json");
             InputStreamReader in = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(in)) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line).append("\n");
            }
        }

        List<PubgLocation> locations = getLocations(parse(json.toString()));
        OptionalDouble maxX = locations.parallelStream()
                .mapToDouble(PubgLocation::getX).max();
        OptionalDouble maxY = locations.parallelStream()
                .mapToDouble(PubgLocation::getY).max();
        OptionalDouble maxZ = locations.parallelStream()
                .mapToDouble(PubgLocation::getZ).max();
        System.out.printf("(%8.2f, %8.2f, %8.2f)", maxX.orElse(0), maxY.orElse(0), maxZ.orElse(0));
    }

    public static List<PubgGameEvent> parse(String json) {
        if (json == null) return null;
        JSONArray object = new JSONArray(json);
        List<PubgGameEvent> eventList = new ArrayList<>();
        for (int i = 0; i < object.length(); i++) {
            eventList.add(PubgGameEvent.parse(object.getJSONObject(i)));
        }
        return eventList;
    }

    public static List<PubgLocation> getLocations(List<PubgGameEvent> eventList) {
        return eventList.parallelStream()
                .filter(gameEvent -> gameEvent.getCharacter() != null)
                .filter(gameEvent -> gameEvent.getCharacter().getLocation() != null)
                .map(gameEvent -> gameEvent.getCharacter().getLocation())
                .collect(Collectors.toList());
    }

    public static String readJson(InputStream stream) throws IOException {
        StringBuilder json = new StringBuilder();
        try (stream;
             InputStreamReader in = new InputStreamReader(stream);
             BufferedReader reader = new BufferedReader(in)) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line).append("\n");
            }
        }
        return json.toString();
    }

}
