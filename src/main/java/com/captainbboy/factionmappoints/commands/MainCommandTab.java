package com.captainbboy.factionmappoints.commands;

import com.captainbboy.factionmappoints.FactionMapPoints;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainCommandTab implements TabCompleter {

    FactionMapPoints plugin;

    public MainCommandTab(FactionMapPoints plg) {
        plugin = plg;
    }

    private final String[] changeBalCommands = new String[]{"give", "set", "remove"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        //create new array
        final List<String> completions = new ArrayList<>();
        List<String> options = new ArrayList<>();

        if(args.length == 1) {
            options.add("get");
            options.add("check");
            options.add("give");
            options.add("set");
            options.add("remove");
            StringUtil.copyPartialMatches(args[0], options, completions);
        }

        if(args.length == 2) {
            if(Arrays.stream(changeBalCommands).anyMatch(x -> x.equalsIgnoreCase(args[0]))) {
                options.add("faction");
                options.add("player");
            } else if(args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("check")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    options.add(p.getName());
                }
            }
            StringUtil.copyPartialMatches(args[1], options, completions);
        }

        if(args.length == 3) {
            if(Arrays.stream(changeBalCommands).anyMatch(x -> x.equalsIgnoreCase(args[0]))) {
                if(args[1].contains("fac")) {
                    for (Faction f : Factions.getInstance().getAllFactions()) {
                        options.add(f.getTag());
                    }
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        options.add(p.getName());
                    }
                }
            }
            StringUtil.copyPartialMatches(args[2], options, completions);
        }

        if(args.length == 4) {
            if(Arrays.stream(changeBalCommands).anyMatch(x -> x.equalsIgnoreCase(args[0]))) {
                for (int i = 0; i < 5; i++) {
                    options.add(String.valueOf(i));
                }
            }
            StringUtil.copyPartialMatches(args[2], options, completions);
        }

        Collections.sort(completions);

        return completions;
    }

}