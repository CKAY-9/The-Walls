package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;
import ca.camerxn.thewalls.Walls.World;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!Game.started) return;
        e.setDeathMessage("");

        Player ply = e.getEntity();
        ply.setGameMode(GameMode.SPECTATOR);
        Team temp = Team.getPlayerTeam(ply);
        if (temp == null) {
            Bukkit.broadcastMessage(Utils.formatText("&c" + ply.getName() + " has been eliminated from The Walls!"));
        } else {
            Bukkit.broadcastMessage(Utils.formatText(temp.teamColor + ply.getName() + " &r&chas been eliminated from The Walls!"));
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(ply.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 1);
            LightningStrike strike = World.world.strikeLightning(ply.getLocation().add(0, 15, 0));
            strike.setVisualFire(false);
            strike.setFireTicks(0);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getPlugin(), Utils::checkWinner, 30L);
    }

}
