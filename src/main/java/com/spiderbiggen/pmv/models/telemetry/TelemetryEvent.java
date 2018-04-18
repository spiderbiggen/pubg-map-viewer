package com.spiderbiggen.pmv.models.telemetry;

import java.util.Arrays;

public enum TelemetryEvent {
    LOG_PLAYER_CREATE("LogPlayerCreate"),
    LOG_PLAYER_LOGIN("LogPlayerLogin"),
    LOG_PLAYER_KILL("LogPlayerKill"),
    LOG_MATCH_START("LogMatchStart"),
    LOG_PLAYER_ATTACK("LogPlayerAttack"),
    LOG_MATCH_END("LogMatchEnd"),
    LOG_VEHICLE_DESTROY("LogVehicleDestroy"),
    LOG_VEHICLE_RIDE("LogVehicleRide"),
    LOG_VEHICLE_LEAVE("LogVehicleLeave"),
    LOG_PLAYER_POSITION("LogPlayerPosition"),
    LOG_PLAYER_TAKE_DAMAGE("LogPlayerTakeDamage"),
    LOG_PLAYER_LOGOUT("LogPlayerLogout"),
    LOG_ITEM_EQUIP("LogItemEquip"),
    LOG_ITEM_PICKUP("LogItemPickup"),
    LOG_ITEM_DROP("LogItemDrop"),
    LOG_ITEM_USE("LogItemUse"),
    LOG_MATCH_DEFINITION("LogMatchDefinition"),
    LOG_ITEM_UNEQUIP("LogItemUnequip"),
    LOG_GAME_STATE_PERIODIC("LogGameStatePeriodic"),
    LOG_ITEM_DETACH("LogItemDetach"),
    LOG_ITEM_ATTACH("LogItemAttach"),
    LOG_CARE_PACKAGE_SPAWN("LogCarePackageSpawn"),
    LOG_CARE_PACKAGE_LAND("LogCarePackageLand");

    private final String jsonName;

    TelemetryEvent(String jsonName) {
        this.jsonName = jsonName;
    }

    public static TelemetryEvent fromString(String string) {
        return Arrays.stream(TelemetryEvent.values())
                .filter(s -> string.equalsIgnoreCase(s.getJsonName()))
                .findAny()
                .orElse(null);
    }

    /**
     * Gets jsonName
     *
     * @return value of jsonName
     */
    public String getJsonName() {
        return jsonName;
    }
}
