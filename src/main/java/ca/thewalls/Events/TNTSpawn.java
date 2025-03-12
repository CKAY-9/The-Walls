package ca.thewalls.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

public class TNTSpawn extends Event {
    public TNTSpawn(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Utils.formatText("&c&lYikes! Watch out for the TNT..."));
            p.playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 255, 1);
            if (!Utils.isAlive(p)) continue;
            for (int i = 0; i < Config.data.getInt("events.tnt.amount"); i++) {
                if (!walls.game.started) {
                    return;
                }

                if (Config.data.getBoolean("events.tnt.followPlayer", true)) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(walls, new Runnable() {
                        @Override
                        public void run() {
                            TNTPrimed primed = (TNTPrimed)walls.world.world.spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                            if (!walls.game.started) {
                                primed.teleport(new Location(walls.world.world, 20_000_000, 600, 20_000_000));
                                primed.remove();
                                return;
                            }
                        }
                    }, (10 * i));
                } else {
                    TNTPrimed primed = (TNTPrimed)walls.world.world.spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                    if (!walls.game.started) {
                        primed.teleport(new Location(walls.world.world, 20_000_000, 600, 20_000_000));
                        primed.remove();
                        return;
                    }
                }
            }
        }
    }
}
