package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
                this.walls.world.world.spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
            }
        }
    }
}
