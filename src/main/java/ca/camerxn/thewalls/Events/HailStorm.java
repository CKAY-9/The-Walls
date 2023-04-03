package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.World;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class HailStormHandler {
    Player p;
    int taskID = 0;
    int timer = Config.data.getInt("events.hailStorm.delay");

    public HailStormHandler(Player p) {
        this.p = p;

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), () -> {
            if (timer <= 0) {
                p.sendMessage(Utils.formatText("&l&3Hail of Arrows incoming!"));

                int amountOfArrows = Config.data.getInt("events.hailStorm.volleySize");
                for (int x = -amountOfArrows; x < amountOfArrows; x++) {
                    for (int z = -amountOfArrows; z < amountOfArrows; z++) {
                        Arrow tempArrow = (Arrow) World.world.spawnEntity(p.getLocation().add(x, Config.data.getInt("events.hailStorm.height"), z), EntityType.ARROW);
                        tempArrow.setCritical(true);
                        tempArrow.setDamage(Config.data.getInt("events.hailStorm.arrowDamage"));
                    }
                }

                Bukkit.getScheduler().cancelTask(taskID);
            }
            timer--;
        }, 0L, 20L);
    }
}

public class HailStorm extends Event {
    public HailStorm(String eventName) {
        super(eventName);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 255, 1);
            p.sendMessage(Utils.formatText("&1A &3&lHail of Arrows&r&1 is dropping in " + Config.data.getInt("events.hailStorm.delay") + " seconds!"));
            if (!Utils.isAlive(p)) continue;
            new HailStormHandler(p);
        }
    }
}
