package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Walls.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (!Game.started) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}
