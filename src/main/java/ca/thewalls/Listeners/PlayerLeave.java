package ca.thewalls.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.thewalls.TheWalls;
import ca.thewalls.Walls.Team;

public class PlayerLeave implements Listener {
    public TheWalls walls;

    public PlayerLeave(TheWalls walls) {
        this.walls = walls;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (!this.walls.game.started) return;
        Team t = Team.getPlayerTeam(e.getPlayer(), this.walls.game.teams);
        if (t == null) return;
        e.getPlayer().setDisplayName(e.getPlayer().getName());
        e.getPlayer().setPlayerListName(e.getPlayer().getName());
        t.members.remove(e.getPlayer());
        if (t.members.size() == 0) {
            this.walls.game.aliveTeams.remove(t);
            this.walls.game.teams.remove(t);
        }
    }
}
