package ca.camerxn.thewalls.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;

public class WLeaderboard implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        HashMap<String, Integer> leadeboardEntries = new HashMap<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : Config.leaderboard.getKeys(false)) {
            leadeboardEntries.put(Config.leaderboard.getString(s + ".username"), Config.leaderboard.getInt(s + ".wins"));
        }
        for (Map.Entry<String, Integer> entry : leadeboardEntries.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list); 
        Collections.reverse(list);
        for (int num : list) {
            for (Entry<String, Integer> entry : leadeboardEntries.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }
        sender.sendMessage(Utils.formatText("&6&lTOP PLAYERS OF THE WALLS"));
        sender.sendMessage(Utils.formatText("&6&l========================"));
        int index = 0;
        for (String key : sortedMap.keySet()) {
            if (index == 5) break;
            sender.sendMessage(Utils.formatText("&5" + key + " - &9" + sortedMap.get(key) + "&5 wins"));
            index++;
        }

        return false;
    }
    
}
