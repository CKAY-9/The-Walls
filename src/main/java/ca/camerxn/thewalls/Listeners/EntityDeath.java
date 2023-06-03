package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EntityDeath implements Listener {
    public TheWalls walls;

    public EntityDeath(TheWalls walls) {
        this.walls = walls;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent e) {
        if (!this.walls.game.started) return;

        // Related to FreeFood event
        if (Config.data.getBoolean("events.gregs.enabled")) {
            if (e.getEntity().getCustomName() == null) return;
            if (!(e.getEntity() instanceof Chicken)) return;
            if (e.getEntity().getCustomName().toLowerCase().contains("greg")) {
                int rnd = new Random().nextInt(10);
                if (rnd == 9 && e.getEntity().getKiller() != null) {
                    e.getEntity().getKiller().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
                    e.getEntity().getKiller().sendMessage(Utils.formatText("&2Greg has spared you and has given you some &e&lGolden Apples!"));
                } else {
                    this.walls.world.world.createExplosion(e.getEntity().getLocation(), Config.data.getInt("events.gregs.power"), Config.data.getBoolean("events.gregs.fireExplosion"));
                }
            }
        }
    }
}
