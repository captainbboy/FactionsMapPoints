package com.captainbboy.factionmappoints;

import com.captainbboy.factionmappoints.SQLite.SQLite;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.UUID;

public class MainUtil {

    public static String cvtStr(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static double getNumber(String strNum) {
        double d;
        try {
            d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return 0.0d;
        }
        return d;
    }

    public static String formatNumber(String str) {
        if(isNumeric(str)) {
            double amount = Double.parseDouble(str);
            DecimalFormat formatter = new DecimalFormat("#,##0.00");

            return formatter.format(amount);
        } else {
            return "is_not_numeric";
        }
    }

    public static String formatNumber(Double num) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(num);
    }

    public static Integer addPoints(SQLite db, UUID uuid, Integer value) {
        String result = db.getPoints(uuid);
        if(result.equals("not_set") || result == null) {
            db.addRowToPoints(uuid, value);
            return value;
        } else {
            if(isNumeric(result)) {
                Double oldValue = getNumber(result);
                db.setPoints(uuid, (oldValue.intValue() + value));
                return (oldValue.intValue() + value);
            } else {
                db.setPoints(uuid, value);
                return value;
            }
        }
    }

    public static Integer setPoints(SQLite db, UUID uuid, Integer value) {
        String result = db.getPoints(uuid);
        if(result.equals("not_set") || result == null) {
            db.addRowToPoints(uuid, value);
            return value;
        } else {
            db.setPoints(uuid, + value);
        }
        return value;
    }

}
