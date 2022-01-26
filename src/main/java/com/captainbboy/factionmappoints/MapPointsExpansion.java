package com.captainbboy.factionmappoints;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapPointsExpansion extends PlaceholderExpansion {

    private final FactionMapPoints plugin;
    private Map<UUID, Integer> map = new HashMap<>();

    public MapPointsExpansion(FactionMapPoints plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "captain_bboy";
    }

    @Override
    public String getIdentifier() {
        return "factionmappoints";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    public void setMap(UUID uuid, Integer integer) {
        if(map.containsKey(uuid)) {
            map.replace(uuid, integer);
        } else {
            map.put(uuid, integer);
        }
    }

    public void removeFromMap(UUID uuid) {
        if(map.containsKey(uuid)) {
            map.remove(uuid);
        }
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("points")){
            if(map.containsKey(player.getUniqueId())) {
                return String.valueOf(map.get(player.getUniqueId()));
            } else {
                String result = plugin.getSQLite().getPoints(player.getUniqueId());
                if (result.equalsIgnoreCase("not_set") || !MainUtil.isNumeric(result))
                    result = "0";
                map.put(player.getUniqueId(), (int)MainUtil.getNumber(result));
                return result;
            }
        }

        return null; // Placeholder is unknown by the Expansion
    }
}