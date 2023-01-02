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
            // fill config
            // events
            if (!data.isSet("events.gregs.enabled")) {
                data.set("events.gregs.enabled", true);
                data.set("events.gregs.timer", 5);
                data.set("events.gregs.power", 3);
                data.set("events.gregs.speed", 3);
                data.set("events.gregs.amount", 5);
            }
            if (!data.isSet("events.reveal.enabled")) {
                data.set("events.reveal.enabled", true);
                data.set("events.reveal.displayCords", true);
                data.set("events.reveal.seconds", 10);
            }
            if (!data.isSet("events.blindSnail.enabled")) {
                data.set("events.blindSnail.enabled", true);
                data.set("events.blindSnail.seconds", 5);
                data.set("events.blindSnail.blindStrength", 10);
                data.set("events.blindSnail.slowStrength", 3);
            }
            if (!data.isSet("events.tnt.enabled")) {
                data.set("events.tnt.enabled", true);
                data.set("events.tnt.amount", 5);
            }
            if (!data.isSet("events.locationSwap.enabled")) {
                data.set("events.locationSwap.enabled", true);
            }
            if (!data.isSet("events.supplyChest.enabled")) {
                data.set("events.supplyChest.enabled", true);
                data.set("events.supplyChest.allowedRegionPercentageOfSize", 0.2);
            }
            if (!data.isSet("events.sinkHole.enabled")) {
                data.set("events.sinkHole.enabled", true);
                data.set("events.sinkHole.size", 1);
                data.set("events.sinkHole.seconds", 3);
                data.set("events.sinkHole.timeUntilReset", 10);
            }
            // players
            if (!data.isSet("players.spawn.steakAmount")) {
                data.set("players.spawn.steakAmount", 8);
            }
            // teams
            if (!data.isSet("teams.zero.name")) {
                data.set("teams.zero.name", "Red");
                data.set("teams.zero.color", "&c");
            }
            if (!data.isSet("teams.one.name")) {
                data.set("teams.one.name", "Blue");
                data.set("teams.one.color", "&9");
            }
            if (!data.isSet("teams.two.name")) {
                data.set("teams.two.name", "Yellow");
                data.set("teams.two.color", "&e");
            }
            if (!data.isSet("teams.three.name")) {
                data.set("teams.three.name", "Green");
                data.set("teams.three.color", "&2");
            }
            if (!data.isSet("teams.allowTie")) {
                data.set("teams.allowTie", false);
            }
            if (!data.isSet("teams.checkWinEverySecond")) {
                data.set("teams.checkWinEverySecond", false);
            }
            // world
            if (!data.isSet("world.borderShrinkPercentageOfSize")) {
                data.set("world.borderShrinkPercentageOfSize", 0.2);
            }
            if (!data.isSet("world.saving")) {
                data.set("world.saving", true);
            }
            data.save(dataFile);
        } catch (IOException ex) {
            Utils.getPlugin().getLogger().warning(ex.toString());
        }
    }
}
