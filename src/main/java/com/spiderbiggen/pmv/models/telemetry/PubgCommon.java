package com.spiderbiggen.pmv.models.telemetry;

import org.json.JSONObject;

public class PubgCommon {
    private String matchId;
    private String mapName;
    private int isGame;

    public static PubgCommon parse(JSONObject object) {
        PubgCommon common = new PubgCommon();
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
