package ca.thewalls.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

class HailStormHandler {
    Player p;
    int taskID = 0;
    int timer = Config.data.getInt("events.hailStorm.delay");
    TheWalls walls;

    public HailStormHandler(Player p, TheWalls walls) {
        this.p = p;
        this.walls = walls;

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), () -> {
            if (timer <= 0) {
                p.sendMessage(Utils.formatText("&l&3Hail of Arrows incoming!"));

                int amountOfArrows = Config.data.getInt("events.hailStorm.volleySize");
                for (int x = -amountOfArrows; x < amountOfArrows; x++) {
                    for (int z = -amountOfArrows; z < amountOfArrows; z++) {
                        Arrow tempArrow = (Arrow) this.walls.world.world.spawnEntity(p.getLocation().add(x, Config.data.getInt("events.hailStorm.height"), z), EntityType.ARROW);
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
    public HailStorm(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 255, 1);
            p.sendMessage(Utils.formatText("&1A &3&lHail of Arrows&r&1 is dropping in " + Config.data.getInt("events.hailStorm.delay") + " seconds!"));
            if (!Utils.isAlive(p)) continue;
            new HailStormHandler(p, this.walls);
        }
    }
}
