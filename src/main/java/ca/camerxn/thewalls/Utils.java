package ca.camerxn.thewalls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class Utils {

    public static String formatText(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String adminMessage(String s) {
        return ChatColor.translateAlternateColorCodes('&', "&6&l[The Walls]&r " + s);
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("TheWalls");
    }

}
