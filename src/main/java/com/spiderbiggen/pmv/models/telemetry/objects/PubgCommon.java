package com.spiderbiggen.pmv.models.telemetry.objects;

import org.json.JSONObject;

public class PubgCommon {
    private final String matchId;
    private final String mapName;
    private final float isGame;

    public PubgCommon(String matchId, String mapName, float isGame) {
        this.matchId = matchId;
        this.mapName = mapName;
        this.isGame = isGame;
    }

    public static PubgCommon parse(JSONObject object) {
        var matchId = object.optString("matchId");
        var mapName = object.optString("mapName");
        var isGame = object.optFloat("isGame");
        return new PubgCommon(matchId, mapName, isGame);
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
     * Gets isGame
     *
     * @return value of isGame
     */
    public float getIsGame() {
        return isGame;
    }

    /**
     * Gets matchId
     *
     * @return value of matchId
     */
    public String getMatchId() {

        return matchId;
    }

}
