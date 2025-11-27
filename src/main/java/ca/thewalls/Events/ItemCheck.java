package ca.thewalls.Events;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

class ItemCheckObjective {
    Material material;
    int quantity;
    int timeInSeconds;

    public ItemCheckObjective(Material material, int quantity, int timeInSeconds) {
        this.material = material;
        this.quantity = quantity;
        this.timeInSeconds = timeInSeconds;
    }
}

class ItemCheckHandler {
    ArrayList<ItemCheckObjective> possibleObjectives;
    Player p;
    ItemCheckObjective currentObjective;
    public TheWalls walls;

    private void fillObjectives() {
        ConfigurationSection objectives = Config.data.getConfigurationSection("events.itemCheck.materials");

        // loop through all high level keys, e.g. just the ids
        for (String key : objectives.getKeys(false)) {
            ConfigurationSection objSection = objectives.getConfigurationSection(key);
            Material mat = Material.matchMaterial(objSection.getString("material", ""));
            if (mat == null) {
                this.walls.getLogger().warning("Failed to match material on " + key + "!");
                continue;
            }

            int quantity = objSection.getInt("quantity", 4);
            int time = objSection.getInt("time", 30);

            possibleObjectives.add(new ItemCheckObjective(mat, quantity, time));
        }
    }

    public ItemCheckHandler(Player p, TheWalls walls) {
        // Build possible maps
        this.possibleObjectives = new ArrayList<>();
        fillObjectives();

        Random rand = new Random();
        this.walls = walls;

        this.p = p;
        this.currentObjective = possibleObjectives.get(rand.nextInt(possibleObjectives.size()));
        this.p.sendMessage(
                Utils.formatText("&cYou must have " + currentObjective.quantity +  " " + this.currentObjective.material.name().replaceAll("_", " ")
                        + "(s) in your inventory within " + currentObjective.timeInSeconds + "s"));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getPlugin(), new Runnable() {
            @Override
            public void run() {
                // stop lightning strike from occuring after game ended
                if (!walls.game.started) {
                    return;
                }

                if (p.getInventory().isEmpty()) {
                    p.sendMessage(Utils.formatText("&cYou failed to get the item! Sucks..."));
                    walls.world.world.spawnEntity(p.getLocation(), EntityType.LIGHTNING);
                    return;
                }

                int count = 0;
                for (ItemStack stack : p.getInventory().getContents()) {
                    if (stack == null)
                        continue;
                    if (stack.getType() == currentObjective.material) {
                        count += stack.getAmount();
                    }
                }

                if (count >= currentObjective.quantity) {
                    p.sendMessage(Utils.formatText("&aYou have enough of the item in your inventory! You are safe."));
                    return;
                }

                p.sendMessage(Utils.formatText("&cYou failed to get the item! Sucks..."));
                walls.world.world.spawnEntity(p.getLocation(), EntityType.LIGHTNING);
                return;
            }
        }, 20 * currentObjective.timeInSeconds);
    }
}

public class ItemCheck extends Event {

    public ItemCheck(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p))
                continue;
            new ItemCheckHandler(p, this.walls);
        }
    }

}
