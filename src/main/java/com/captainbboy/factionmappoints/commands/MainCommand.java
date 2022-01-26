package com.captainbboy.factionmappoints.commands;

import com.captainbboy.factionmappoints.FactionMapPoints;
import com.captainbboy.factionmappoints.MainUtil;
import com.captainbboy.factionmappoints.SQLite.SQLite;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class MainCommand implements CommandExecutor {

    FactionMapPoints plugin;

    public MainCommand(FactionMapPoints plg) {
        this.plugin = plg;
    }

    private enum TYPE {
        ADD,
        SET,
        REMOVE
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("mappoints")) {
            if(sender.hasPermission("mappoints.managepoints")) {
                if(args.length == 0) {
                    sendHelpCommand(sender, null);
                } else if(args[0].equalsIgnoreCase("give")) {
                    managePoints(sender, command, args, TYPE.ADD);
                } else if(args[0].equalsIgnoreCase("set")) {
                    managePoints(sender, command, args, TYPE.SET);
                } else if(args[0].equalsIgnoreCase("remove")) {
                    managePoints(sender, command, args, TYPE.REMOVE);
                } else if(args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("get") ) {
                    if(args.length > 1) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        if(target != null) {
                            String result = this.plugin.getSQLite().getPoints(target.getUniqueId());
                            if(result.equalsIgnoreCase("not_set"))
                                result = "0";
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&a&l(!) &a"+target.getName()+" has "+result+" map points!"
                            ));
                        } else {
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&c&l(!) &cThat is not a valid player."
                            ));
                            return true;
                        }
                    } else
                        sendHelpCommand(sender, "check");
                } else
                    sendHelpCommand(sender, null);
                return true;
            } else {
                sender.sendMessage(MainUtil.cvtStr(
                        "&c&l(!) &cNo permission!"
                ));
            }
        }
        return true;
    }

    private void managePoints(CommandSender sender, Command command, String[] args, TYPE type) {
        if(args.length > 1) {
            if(args[1].equalsIgnoreCase("player") || args[1].equalsIgnoreCase("player")) {
                if(args.length > 2) {
                    int amount = 1;
                    if(args.length > 3) {
                        if(MainUtil.isNumeric(args[3])) {
                            amount = (int)MainUtil.getNumber(args[3]);
                        } else {
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&c&l(!) &cThat is not a valid number."
                            ));
                            return;
                        }
                    }
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                    if(target != null) {
                        SQLite db = this.plugin.getSQLite();
                        if(type == TYPE.ADD) {
                            MainUtil.addPoints(db, target.getUniqueId(), amount);
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&a&l(!) &aSuccessfully added "+amount+" to "+target.getName()+"'s map points."
                            ));
                        } else if(type == TYPE.REMOVE) {
                            MainUtil.addPoints(db, target.getUniqueId(), -amount);
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&a&l(!) &aSuccessfully removed "+amount+" from "+target.getName()+"'s map points."
                            ));
                        } else {
                            MainUtil.setPoints(db, target.getUniqueId(), amount);
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&a&l(!) &aSuccessfully set "+target.getName()+"'s map points to "+amount+"."
                            ));
                        }
                    } else {
                        sender.sendMessage(MainUtil.cvtStr(
                                "&c&l(!) &cThat is not a valid player."
                        ));
                        return;
                    }
                } else
                    sendHelpCommand(sender, args[0]);
            } else if(args[1].equalsIgnoreCase("faction") || args[1].equalsIgnoreCase("fac")) {
                if(args.length > 2) {
                    Faction fac = Factions.getInstance().getByTag(args[2]);
                    if(fac != null && fac.isNormal()) {
                        Set<FPlayer> players = fac.getFPlayers();
                        int amount = 1;
                        if(args.length > 3) {
                            if(MainUtil.isNumeric(args[3])) {
                                amount = (int)MainUtil.getNumber(args[3]);
                            } else {
                                sender.sendMessage(MainUtil.cvtStr(
                                        "&c&l(!) &cThat is not a valid number."
                                ));
                                return;
                            }
                        }
                        if(players.size() > 0) {
                            SQLite db = this.plugin.getSQLite();
                            int finalAmount = amount;
                            players.forEach((FPlayer player) -> {
                                if(type == TYPE.ADD) {
                                    MainUtil.addPoints(db, player.getOfflinePlayer().getUniqueId(), finalAmount);
                                    sender.sendMessage(MainUtil.cvtStr(
                                            "&a&l(!) &aSuccessfully added "+finalAmount+" to every member of "+args[2]+"'s map points."
                                    ));
                                } else if(type == TYPE.REMOVE) {
                                    MainUtil.addPoints(db, player.getOfflinePlayer().getUniqueId(), -finalAmount);
                                    sender.sendMessage(MainUtil.cvtStr(
                                            "&a&l(!) &aSuccessfully removed "+finalAmount+" from every member of "+args[2]+"'s map points."
                                    ));
                                } else {
                                    MainUtil.setPoints(db, player.getOfflinePlayer().getUniqueId(), finalAmount);
                                    sender.sendMessage(MainUtil.cvtStr(
                                            "&a&l(!) &aSuccessfully set every member of "+args[2]+"'s to "+finalAmount+" map points."
                                    ));
                                }
                            });
                        } else
                            sender.sendMessage(MainUtil.cvtStr(
                                    "&c&l(!) &cThat faction has no members."
                            ));
                    } else
                        sender.sendMessage(MainUtil.cvtStr(
                                "&c&l(!) &cInvalid faction name."
                        ));
                } else
                    sendHelpCommand(sender, args[0]);
            } else
                sendHelpCommand(sender, args[0]);
        } else
            sendHelpCommand(sender, args[0]);
    }

    private void sendHelpCommand(CommandSender sender, String requiredCommand) {
        sender.sendMessage(MainUtil.cvtStr("&8&m------------------------------------"));
        sender.sendMessage("");
        sender.sendMessage(MainUtil.cvtStr("          &7Version &a["+this.plugin.currVersion+"] &7by &a&ncaptain_bboy"));
        sender.sendMessage("");
        if(requiredCommand == null || requiredCommand.toLowerCase().contains("get") || requiredCommand.toLowerCase().contains("check")) {
            sender.sendMessage(MainUtil.cvtStr("&e&l(!) &7/mappoints get <playerName>"));
        }
        if(requiredCommand == null || requiredCommand.toLowerCase().contains("give")) {
            sender.sendMessage(MainUtil.cvtStr("&e&l(!) &7/mappoints give &e<faction/player> <factionName/playerName> [points=1]"));
        }
        if(requiredCommand == null || requiredCommand.toLowerCase().contains("set")) {
            sender.sendMessage(MainUtil.cvtStr("&e&l(!) &7/mappoints set &e<faction/player> <factionName/playerName> [points=1]"));
        }
        if(requiredCommand == null || requiredCommand.toLowerCase().contains("remove")) {
            sender.sendMessage(MainUtil.cvtStr("&e&l(!) &7/mappoints remove &e<faction/player> <factionName/playerName> [points=1]"));
        }
        sender.sendMessage("");
        sender.sendMessage(MainUtil.cvtStr("&8&m------------------------------------"));
    }

}
