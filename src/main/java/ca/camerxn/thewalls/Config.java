package ca.camerxn.thewalls;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    public static File dataFile;
    public static YamlConfiguration data;

    public static void initializeData() {
        try {
            dataFile = new File(Utils.getPlugin().getDataFolder(), "config.yml");
            if (!dataFile.exists()) {
                if (dataFile.getParentFile().mkdirs()) {
                    Utils.getPlugin().getLogger().info("Created data folder!");
                }
                if (dataFile.createNewFile()) {
                    Utils.getPlugin().getLogger().info("Created config file!");
                }
            }
            data = YamlConfiguration.loadConfiguration(dataFile);
            if (dataFile.length() <= 0) {
                // fill config
                // events
                data.set("events.gregs.enabled", true);
                data.set("events.gregs.timer", 5);
                data.set("events.gregs.power", 3);
                data.set("events.gregs.speed", 3);
                data.set("events.gregs.amount", 5);
                data.set("events.reveal.enabled", true);
                data.set("events.reveal.displayCords", true);
                data.set("events.reveal.seconds", 10);
                data.set("events.blindSnail.enabled", true);
                data.set("events.blindSnail.seconds", 5);
                data.set("events.blindSnail.blindStrength", 10);
                data.set("events.blindSnail.slowStrength", 3);
                data.set("events.tnt.enabled", true);
                data.set("events.tnt.amount", 5);
                data.set("events.locationSwap.enabled", true);
                data.set("events.supplyChest.enabled", true);
                data.set("events.supplyChest.allowedRegionPercentageOfSize", 0.2);
                // players
                data.set("players.spawn.steakAmount", 8);
                // teams
                data.set("teams.zero.name", "Red");
                data.set("teams.zero.color", "&c");
                data.set("teams.one.name", "Blue");
                data.set("teams.one.color", "&9");
                data.set("teams.two.name", "Yellow");
                data.set("teams.two.color", "&e");
                data.set("teams.three.name", "Green");
                data.set("teams.three.color", "&2");
                data.set("teams.allowTie", false);
                // world
                data.set("world.borderShrinkPercentageOfSize", 0.2);
                data.set("world.saving", true);

                Utils.getPlugin().getLogger().info("Setup default config!");
                data.save(dataFile);
            }
        } catch (IOException ex) {
            Utils.getPlugin().getLogger().warning(ex.toString());
        }
    }
}
