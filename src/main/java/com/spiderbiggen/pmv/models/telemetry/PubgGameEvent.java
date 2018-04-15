package com.spiderbiggen.pmv.models.telemetry;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PubgGameEvent implements Comparable<PubgGameEvent> {

    private static final DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;

    private final PubgCharacter character;
    private final PubgCommon common;
    private final TelemetryEvent type; // _T
    private final LocalDateTime dateTime; // _D

    public PubgGameEvent(PubgCharacter character, PubgCommon common, TelemetryEvent type, LocalDateTime dateTime) {
        this.character = character;
        this.common = common;
        this.type = type;
        this.dateTime = dateTime;
    }

    public static PubgGameEvent parse(JSONObject object) {
        PubgCharacter character = null;
        if (object.has("character")) {
            character = PubgCharacter.parse(object.getJSONObject("character"));
        }
        PubgCommon common = null;
        if (object.has("common")) {
            common = PubgCommon.parse(object.getJSONObject("common"));
        }
        TelemetryEvent _t = TelemetryEvent.fromString(object.getString("_T"));
        String _d = object.getString("_D");
        LocalDateTime dateTime = LocalDateTime.from(format.parse(_d));
        return new PubgGameEvent(character, common, _t, dateTime);
    }

    /**
     * Gets character
     *
     * @return value of character
     */
    public PubgCharacter getCharacter() {
        return character;
    }

    /**
     * Gets common
     *
     * @return value of common
     */
    public PubgCommon getCommon() {
        return common;
    }

    /**
     * Gets type
     *
     * @return value of type
     */
    public TelemetryEvent getType() {
        return type;
    }

    /**
     * Gets dateTime
     *
     * @return value of dateTime
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(PubgGameEvent o) {
        return getDateTime().compareTo(o.getDateTime());
    }

}
