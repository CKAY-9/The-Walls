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
        ply.setGameMode(GameMode.SPECTATOR);
        Bukkit.broadcastMessage(Utils.formatText("&c" + ply.getName() + " has been eliminated from The Walls!"));

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
