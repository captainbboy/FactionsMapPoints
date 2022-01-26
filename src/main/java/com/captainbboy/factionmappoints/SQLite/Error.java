package com.captainbboy.factionmappoints.SQLite;

import com.captainbboy.factionmappoints.FactionMapPoints;

import java.util.logging.Level;

public class Error {
    public static void execute(FactionMapPoints plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(FactionMapPoints plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}