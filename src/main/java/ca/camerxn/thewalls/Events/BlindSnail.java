package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.World;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlindSnail extends Event {
    public BlindSnail(String eventName) {
        super(eventName);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Utils.formatText("&5You have been turned into a &c&lBLIND SNAIL&r&5."));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 10, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 3, true));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 255, 1);
        }
    }
}
