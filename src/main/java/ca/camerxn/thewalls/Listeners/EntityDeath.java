package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.World;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityDeath implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent e) {
        if (!Game.started) return;

        // Related to FreeFood event
        if (e.getEntity().getCustomName() == null) return;
        if (!(e.getEntity() instanceof Chicken)) return;
        if (e.getEntity().getCustomName().toLowerCase().contains("greg")) {
            int rnd = new Random().nextInt(10);
            if (rnd == 9 && e.getEntity().getKiller() != null) {
                e.getEntity().getKiller().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
                e.getEntity().getKiller().sendMessage(Utils.formatText("&2Greg has spared you and has given you some &e&lGolden Apples!"));
            } else {
                World.world.createExplosion(e.getEntity().getLocation(), 4, true);
            }
        }
    }
}
