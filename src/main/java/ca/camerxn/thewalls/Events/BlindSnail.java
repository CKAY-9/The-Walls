package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Config;
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
            if (!Utils.isAlive(p)) continue;
            p.sendMessage(Utils.formatText("&5You have been turned into a &c&lBLIND SNAIL&r&5."));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Config.data.getInt("events.blindSnail.seconds") * 20, Config.data.getInt("events.blindSnail.blindStrength"), true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Config.data.getInt("events.blindSnail.seconds") * 20, Config.data.getInt("events.blindSnail.slowStrength"), true));
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 255, 1);
        }
    }
}
