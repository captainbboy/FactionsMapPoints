package com.captainbboy.factionmappoints.events;

import com.captainbboy.factionmappoints.FactionMapPoints;
import com.captainbboy.factionmappoints.MainUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveEvent implements Listener {

    private FactionMapPoints plugin;

    public PlayerJoinLeaveEvent(FactionMapPoints plg) {
        plugin = plg;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        String result = plugin.getSQLite().getPoints(e.getPlayer().getUniqueId());
        Integer integer;
        if(MainUtil.isNumeric(result))
            integer = (int)MainUtil.getNumber(result);
        else
            integer = 0;
        plugin.getMapPointsExpansion().setMap(e.getPlayer().getUniqueId(), integer);
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent e) {
        plugin.getMapPointsExpansion().removeFromMap(e.getPlayer().getUniqueId());
    }

}
