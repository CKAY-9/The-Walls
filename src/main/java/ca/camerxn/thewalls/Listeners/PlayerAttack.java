package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttack implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (!Game.started) return;

        Player attacker = (Player) e.getDamager();
        Player receiver = (Player) e.getEntity();

        if (Team.getPlayerTeam(attacker) == Team.getPlayerTeam(receiver)) {
            e.setDamage(0); // Cancel damage, but still allow for knock-back
        }
    }
}
