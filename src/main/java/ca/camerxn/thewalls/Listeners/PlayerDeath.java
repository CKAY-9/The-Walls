package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!Game.started) return;
        e.setDeathMessage("");

        Player ply = e.getEntity();
        ply.spigot().respawn();
        ply.setGameMode(GameMode.SPECTATOR);
        Bukkit.broadcastMessage(Utils.formatText("&c" + ply.getName() + " has been eliminated from The Walls!"));

        for (Team t : Game.aliveTeams) {
            for (Player p : t.members) {
                t.alive = false;
                if (p.getGameMode() != GameMode.SPECTATOR || p.getStatistic(Statistic.DEATHS) <= 0) {
                    t.alive = true;
                    break;
                }
            }
            if (!t.alive) {
                Bukkit.broadcastMessage(Utils.formatText(t.teamColor + t.teamName + "&c team has been eliminated from The Walls!"));
                Game.aliveTeams.remove(t);
                break;
            }
        }

        if (Game.aliveTeams.size() == 1) {
            Team winningTeam = Game.aliveTeams.get(0);
            Bukkit.broadcastMessage(Utils.formatText(winningTeam.teamColor + winningTeam.teamName + "&2 has won The Walls!"));
            Game.end();
        }

    }

}
