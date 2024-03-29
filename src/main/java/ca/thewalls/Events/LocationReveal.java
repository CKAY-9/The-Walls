package ca.thewalls.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;
import ca.thewalls.Walls.Team;

import java.util.Random;

public class LocationReveal extends Event {
    public LocationReveal(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Team t : this.walls.game.aliveTeams) {
            if (!t.alive) continue;
            if (t.members.size() == 0) continue;
            int r = new Random().nextInt(t.members.size());
            Player p = t.members.get(r);
            while (!Utils.isAlive(p) && t.members.size() >= 2) {
                r = new Random().nextInt(t.members.size());
                p = t.members.get(r);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * Config.data.getInt("events.reveal.seconds"), 2));
            for (Player _p : Bukkit.getOnlinePlayers()) {
                if (Config.data.getBoolean("events.reveal.displayCords")) {
                    _p.sendMessage(Utils.formatText(t.teamColor + p.getName() + "&r of the " + t.teamColor + t.teamName + " team&r is at &6" + p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() + "&r." ));
                }
                _p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 1);
            }
        }
    }
}
