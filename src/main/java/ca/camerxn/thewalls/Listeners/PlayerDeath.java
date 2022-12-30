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

        Utils.checkWinner();
    }

}
