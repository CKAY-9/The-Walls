package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    public TheWalls walls;

    public PlayerDeath(TheWalls walls) {
        this.walls = walls;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!this.walls.game.started) return;
        e.setDeathMessage("");

        Player ply = e.getEntity();
        ply.setGameMode(GameMode.SPECTATOR);
        Team temp = Team.getPlayerTeam(ply, this.walls.game.teams);
        if (temp == null) {
            Bukkit.broadcastMessage(Utils.formatText("&c" + ply.getName() + " has been eliminated from The Walls!"));
        } else {
            Bukkit.broadcastMessage(Utils.formatText(temp.teamColor + ply.getName() + " &r&chas been eliminated from The Walls!"));
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(ply.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 1);
            LightningStrike strike = this.walls.world.world.strikeLightning(ply.getLocation().add(0, 15, 0));
            strike.setVisualFire(false);
            strike.setFireTicks(0);
        }

        this.walls.utils.checkWinner();
    }

}
