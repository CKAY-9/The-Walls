package ca.camerxn.thewalls;

import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {

    public static String formatText(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String adminMessage(String s) {
        return ChatColor.translateAlternateColorCodes('&', "&6&l[The Walls]&r " + s);
    }

    public static boolean isAlive(Player p) {
        return p.getGameMode() != GameMode.SPECTATOR || p.getStatistic(Statistic.DEATHS) <= 0;
    }

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("TheWalls");
    }

    public static void checkWinner() {
        int i = 0;
        for (Team t : Game.aliveTeams) {
            for (Player p : t.members) {
                t.alive = false;
                if (Utils.isAlive(p)) {
                    t.alive = true;
                    break;
                }
            }
            Utils.getPlugin().getLogger().info(t.alive + " / " + t.teamName);
            if (!t.alive) {
                Bukkit.broadcastMessage(Utils.formatText(t.teamColor + t.teamName + "&c team has been eliminated from The Walls!"));
                Game.aliveTeams.remove(i);
            }
            i++;
        }

        if (Game.aliveTeams.size() <= 1) {
            Team winningTeam = Game.aliveTeams.get(0);
            Bukkit.broadcastMessage(Utils.formatText(winningTeam.teamColor + winningTeam.teamName + "&2 has won The Walls!"));
            Game.end();
        }
    }

}
