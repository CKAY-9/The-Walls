package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
        int c = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p)) continue;
            c = (c + 1) % locations.size(); // Loop back around to 0
            p.teleport(locations.get(c));
            p.sendMessage(Utils.formatText("&9You have been teleported to a player's previous position!"));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 255, 1);
        }
    }
}
