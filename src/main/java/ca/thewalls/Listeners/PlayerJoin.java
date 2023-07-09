package ca.thewalls.Listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

public class PlayerJoin implements Listener {
    public TheWalls walls;

    public PlayerJoin(TheWalls walls) {
        this.walls = walls;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Config.createLeaderboardPlayer(e.getPlayer());

        if (!this.walls.game.started) return;
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
        e.getPlayer().sendMessage(Utils.formatText("&cThere is currently a Walls game going on!"));
    }
}
