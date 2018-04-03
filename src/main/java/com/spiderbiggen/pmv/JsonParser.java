package com.spiderbiggen.pmv;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class JsonParser {

    public static void main(String... args) throws IOException {
        StringBuilder json = new StringBuilder();
        try (InputStream inputStream = JsonParser.class.getResourceAsStream("/pc-telemetry.json");
             InputStreamReader in = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(in)) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line).append("\n");
            }
        }

        List<Location> locations = getLocations(parse(json.toString()));
        OptionalDouble maxX = locations.parallelStream()
                .mapToDouble(Location::getX).max();
        OptionalDouble maxY = locations.parallelStream()
                .mapToDouble(Location::getY).max();
        OptionalDouble maxZ = locations.parallelStream()
                .mapToDouble(Location::getZ).max();
        System.out.printf("(%8.2f, %8.2f, %8.2f)", maxX.orElse(0), maxY.orElse(0), maxZ.orElse(0));
    }

    public static List<GameEvent> parse(String json) {
        JSONArray object = new JSONArray(json);
        List<GameEvent> eventList = new ArrayList<>();
        for (int i = 0; i < object.length(); i++) {
            eventList.add(GameEvent.parse(object.getJSONObject(i)));
        }
        return eventList;
    }

    public static List<Location> getLocations(List<GameEvent> eventList) {
        return eventList.parallelStream()
                .filter(gameEvent -> gameEvent.getCharacter() != null)
                .filter(gameEvent -> gameEvent.getCharacter().getLocation() != null)
                .map(gameEvent -> gameEvent.getCharacter().getLocation())
                .collect(Collectors.toList());
    }

    public static String readJson(InputStream stream) throws IOException {
        StringBuilder json = new StringBuilder();
        try (InputStream inputStream = stream;
             InputStreamReader in = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(in)) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line).append("\n");
            }
        }
        return json.toString();
    }

    public static class GameEvent {
        private Character character;
        private Common common;
        private String type; // _T

        public static GameEvent parse(JSONObject object) {
            GameEvent event = new GameEvent();
            if (object.has("character")) {
                event.setCharacter(Character.parse(object.getJSONObject("character")));
            }
            if (object.has("common")) {
                event.setCommon(Common.parse(object.getJSONObject("common")));
            }
            event.setType(object.getString("_T"));
            return event;
        }

        /**
         * Gets character
         *
         * @return value of character
         */
        public Character getCharacter() {
            return character;
        }

        /**
         * Sets character.
         *
         * @param character the new value of character
         */
        public void setCharacter(Character character) {
            this.character = character;
        }

        /**
         * Gets common
         *
         * @return value of common
         */
        public Common getCommon() {
            return common;
        }

        /**
         * Sets common.
         *
         * @param common the new value of common
         */
        public void setCommon(Common common) {
            this.common = common;
        }

        /**
         * Gets type
         *
         * @return value of type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the new value of type
         */
        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Character {
        String name;
        int teamId;
        double health;
        Location location;
        int ranking;
        String accountId;

        public static Character parse(JSONObject object) {
            Character character = new Character();
            character.setName(object.getString("name"));
            character.setTeamId(object.getInt("teamId"));
            character.setHealth(object.getDouble("health"));
            character.setRanking(object.getInt("ranking"));
            character.setAccountId(object.getString("accountId"));
            if (object.has("location")) {
                character.setLocation(Location.parse(object.getJSONObject("location")));
            }
            return character;
        }

        /**
         * Gets name
         *
         * @return value of name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the new value of name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets teamId
         *
         * @return value of teamId
         */
        public int getTeamId() {
            return teamId;
        }

        /**
         * Sets teamId.
         *
         * @param teamId the new value of teamId
         */
        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        /**
         * Gets health
         *
         * @return value of health
         */
        public double getHealth() {
            return health;
        }

        /**
         * Sets health.
         *
         * @param health the new value of health
         */
        public void setHealth(double health) {
            this.health = health;
        }

        /**
         * Gets location
         *
         * @return value of location
         */
        public Location getLocation() {
            return location;
        }

        /**
         * Sets location.
         *
         * @param location the new value of location
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * Gets ranking
         *
         * @return value of ranking
         */
        public int getRanking() {
            return ranking;
        }

        /**
         * Sets ranking.
         *
         * @param ranking the new value of ranking
         */
        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        /**
         * Gets accountId
         *
         * @return value of accountId
         */
        public String getAccountId() {
            return accountId;
        }

        /**
         * Sets accountId.
         *
         * @param accountId the new value of accountId
         */
        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }
    }

    public static class Common {
        private String matchId;
        private String mapName;
        private int isGame;

        public static Common parse(JSONObject object) {
            Common common = new Common();
            common.setMatchId(object.optString("matchId"));
            common.setMapName(object.optString("mapName"));
            common.setIsGame(object.optInt("isGame"));
            return common;
        }

        /**
         * Gets mapName
         *
         * @return value of mapName
         */
        public String getMapName() {
            return mapName;
        }

        /**
         * Sets mapName.
         *
         * @param mapName the new value of mapName
         */
        public void setMapName(String mapName) {
            this.mapName = mapName;
        }

        /**
         * Gets isGame
         *
         * @return value of isGame
         */
        public int getIsGame() {
            return isGame;
        }

        /**
         * Sets isGame.
         *
         * @param isGame the new value of isGame
         */
        public void setIsGame(int isGame) {
            this.isGame = isGame;
        }

        /**
         * Gets matchId
         *
         * @return value of matchId
         */
        public String getMatchId() {

            return matchId;
        }

        /**
         * Sets matchId.
         *
         * @param matchId the new value of matchId
         */
        public void setMatchId(String matchId) {
            this.matchId = matchId;
        }
    }

    public static class Location {
        double x;
        double y;
        double z;

        public static Location parse(JSONObject object) {
            Location location = new Location();
            location.setX(object.getDouble("x"));
            location.setY(object.getDouble("y"));
            location.setZ(object.getDouble("z"));
            return location;
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
         * Sets x.
         *
         * @param x the new value of x
         */
        public void setX(double x) {
            this.x = x;
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
         * Sets y.
         *
         * @param y the new value of y
         */
        public void setY(double y) {
            this.y = y;
        }

        /**
         * Gets z
         *
         * @return value of z
         */
        public double getZ() {
            return z;
        }

        /**
         * Sets z.
         *
         * @param z the new value of z
         */
        public void setZ(double z) {
            this.z = z;
        }

        public Location getNormalized(double max) {
            Location location = new Location();
            location.setX(x / max);
            location.setY(y / max);
            location.setZ(z / max);
            return location;
        }
    }
}
