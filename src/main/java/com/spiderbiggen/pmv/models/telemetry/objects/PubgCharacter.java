package com.spiderbiggen.pmv.models.telemetry.objects;

import com.spiderbiggen.pmv.models.PubgUser;
import org.json.JSONObject;

public class PubgCharacter extends PubgUser {
    private final int teamId;
    private final double health;
    private final PubgLocation location;
    private final int ranking;

    public PubgCharacter(String name, int teamId, double health, PubgLocation location, int ranking, String accountId) {
        super(name, accountId);
        this.teamId = teamId;
        this.health = health;
        this.location = location;
        this.ranking = ranking;
    }

    public static PubgCharacter parse(JSONObject object) {
        String name = object.getString("name");
        int teamId = object.getInt("teamId");
        double health = object.getDouble("health");
        int ranking = object.getInt("ranking");
        String accountId = object.getString("accountId");
        PubgLocation location = null;
        if (object.has("location")) {
            location = PubgLocation.parse(object.getJSONObject("location"));
        }
        return new PubgCharacter(name, teamId, health, location, ranking, accountId);
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
     * Gets health
     *
     * @return value of health
     */
    public double getHealth() {
        return health;
    }

    /**
     * Gets location
     *
     * @return value of location
     */
    public PubgLocation getLocation() {
        return location;
    }

    /**
     * Gets ranking
     *
     * @return value of ranking
     */
    public int getRanking() {
        return ranking;
    }

}