package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Config.createLeaderboardPlayer(e.getPlayer());

        if (!Game.started) return;
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
        e.getPlayer().sendMessage(Utils.formatText("&cThere is currently a Walls game going on!"));
    }
}
