package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class LocationSwap extends Event {
    public LocationSwap(String eventName) {
        super(eventName);
    }

    @Override
    public void run() {
        ArrayList<Location> locations = new ArrayList<>();
        // fill locations of players
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p)) continue;
            locations.add(p.getLocation());
        }
        
        Collections.shuffle(locations);

        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p)) continue;
            p.teleport(locations.get(i));
            p.sendMessage(Utils.formatText("&9You have been teleported to a player's previous position!"));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 255, 1);
            i++;
        }
    }
}
