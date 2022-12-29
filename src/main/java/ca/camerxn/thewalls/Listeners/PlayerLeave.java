package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (!Game.started) return;
        Team t = Team.getPlayerTeam(e.getPlayer());
        if (t == null) return;
        e.getPlayer().setDisplayName(e.getPlayer().getName());
        e.getPlayer().setPlayerListName(e.getPlayer().getName());
        t.members.remove(e.getPlayer());
        if (t.members.size() == 0) {
            Game.aliveTeams.remove(t);
            Game.teams.remove(t);
        }
    }
}
