package ca.camerxn.thewalls;

import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;

import java.util.ArrayList;

import org.bukkit.*;
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
        ArrayList<Integer> teamsToRemove = new ArrayList<>();
        for (Team t : Game.aliveTeams) {
            for (Player p : t.members) {
                t.alive = false;
                if (Utils.isAlive(p)) {
                    t.alive = true;
                    break;
                }
            }
            if (!t.alive) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 255, 1);
                }
                Bukkit.broadcastMessage(Utils.formatText(t.teamColor + t.teamName + "&c team has been eliminated from The Walls!"));
                teamsToRemove.add(i);
            }
            i++;
        }
        for (int j = 0; j < teamsToRemove.size(); j++) {
            Utils.getPlugin().getLogger().info(Game.aliveTeams.get(j).teamName + " has been eliminated!");
            Game.aliveTeams.remove(j);
        }
        if (Config.data.getBoolean("teams.allowTie") || Bukkit.getOnlinePlayers().size() <= 1) {
            if (Game.aliveTeams.size() == 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 255, 1);
                }
                Bukkit.broadcastMessage(Utils.formatText("&6&lThe Walls has resulted in a tie!"));
                Game.end(false, null);
            }
        }
        if (Game.aliveTeams.size() == 1) {
            Team winningTeam = Game.aliveTeams.get(0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 255, 1);
            }
            Bukkit.broadcastMessage(Utils.formatText(winningTeam.teamColor + winningTeam.teamName + "&2 has won The Walls!"));
            Game.end(false, null);
        }

    }

}
