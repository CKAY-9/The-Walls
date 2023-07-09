package ca.thewalls.Events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

class ItemCheckHandler {
    Material[] possibleMats = {
        Material.DIRT,
        Material.OAK_LOG,
        Material.DIAMOND,
        Material.STONE,
        Material.COBBLESTONE,
        Material.STRING,
        Material.LAPIS_LAZULI
    };
    Player p;
    Material chosenMat;
    public TheWalls walls;

    public ItemCheckHandler(Player p, TheWalls walls) {
        Random rand = new Random();
        this.walls = walls;

        this.p = p;
        this.chosenMat = possibleMats[rand.nextInt(possibleMats.length)];
        this.p.sendMessage(Utils.formatText("&cYou must have " + this.chosenMat.name().replaceAll("_", " ") + " in your inventory within " + Config.data.getInt("events.itemCheck.timeLimit") + "s"));
    
        Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getPlugin(), new Runnable() {
            @Override
            public void run() {
                // stop lightning strike from occuring after game ended
                if (!walls.game.started) {
                    return;
                }

                boolean flag = false;

                for (ItemStack stack : p.getInventory().getContents()) {
                    if (stack == null) continue;
                    if (stack.getType() == chosenMat) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    p.sendMessage(Utils.formatText("&aYou have the chosen item in your inventory!"));
                    return;
                }

                p.sendMessage(Utils.formatText("&cYou failed to get the item!"));
                walls.world.world.spawnEntity(p.getLocation(), EntityType.LIGHTNING);
                return;
            }
        }, 20 * Config.data.getInt("events.itemCheck.timeLimit"));
    }
}

public class ItemCheck extends Event {

    public ItemCheck(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p)) continue;
            new ItemCheckHandler(p, this.walls);
        }
    }
    
}
