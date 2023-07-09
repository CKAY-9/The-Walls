package ca.thewalls;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Config {
    public static File dataFile;
    public static YamlConfiguration data;
    public static File leaderboardFile;
    public static YamlConfiguration leaderboard;

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
            if (!data.isSet("events.gregs.fireExplosion")) {
                data.set("events.gregs.fireExplosion", true);
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
            if (!data.isSet("events.hailStorm.enabled")) {
                data.set("events.hailStorm.enabled", true);
                data.set("events.hailStorm.delay", 5);
                data.set("events.hailStorm.height", 5);
                data.set("events.hailStorm.volleySize", 3);
                data.set("events.hailStorm.arrowDamage", 4);
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
                data.set("teams.checkWinEverySecond", true);
            }
            // world
            if (!data.isSet("world.borderShrinkPercentageOfSize")) {
                data.set("world.borderShrinkPercentageOfSize", 0.2);
            }
            if (!data.isSet("world.saving")) {
                data.set("world.saving", true);
            }
            if (!data.isSet("theWalls.legacyHud")) {
                data.set("theWalls.legacyHud", false);
            }
            if (!data.isSet("theWalls.respawnDuringPrepTime")) {
                data.set("theWalls.respawnDuringPrepTime", false);
            }
            if (!data.isSet("theWalls.respawnDuringInitialFighting")) {
                data.set("theWalls.respawnDuringInitialFighting", false);
            }
            if (!data.isSet("theWalls.autoExecute.center")) {
                data.set("theWalls.autoExecute.center.x", 0);
                data.set("theWalls.autoExecute.center.z", 0);
                data.set("theWalls.autoExecute.size", 100);
                data.set("theWalls.autoExecute.eventCooldown", 60);
                data.set("theWalls.autoExecute.prepTime", 600);
                data.set("theWalls.autoExecute.timeUntilBorderClose", 600);
                data.set("theWalls.autoExecute.speedOfBorderClose", 180);
                data.set("theWalls.autoExecute.worldName", "world");
            }
            if (!data.isSet("events.bossMan.enabled")) {
                data.set("events.bossMan.enabled", true);
                data.set("events.bossMan.prepTime", 3);
                data.set("events.bossMan.dropRate", 0.35f);
                data.set("events.bossMan.protectionLevel", 3);
                data.set("events.bossMan.resistance", 1);
                data.set("events.bossMan.name", "Boss Man Jim");
            }
            if (!data.isSet("events.itemCheck.enabled")) {
                data.set("events.itemCheck.enabled", true);
                data.set("events.itemCheck.timeLimit", 30);
            }

            data.save(dataFile);

            leaderboardFile = new File(Utils.getPlugin().getDataFolder(), "leaderboard.yml");
            if (!leaderboardFile.exists()) {
                if (leaderboardFile.getParentFile().mkdirs()) {
                    Utils.getPlugin().getLogger().info("Created data folder!");
                }
                if (leaderboardFile.createNewFile()) {
                    Utils.getPlugin().getLogger().info("Created leadeboard file!");
                }
            }
            leaderboard = YamlConfiguration.loadConfiguration(leaderboardFile);

            for (Player p : Bukkit.getOnlinePlayers()) {
                createLeaderboardPlayer(p);
            }
        } catch (IOException ex) {
            Utils.getPlugin().getLogger().warning(ex.toString());
        }
    }

    public static void createLeaderboardPlayer(Player p) {
        if (!Config.leaderboard.isSet(p.getPlayer().getUniqueId().toString() + ".wins")) {
            Config.leaderboard.set(p.getPlayer().getUniqueId().toString() + ".wins", 0);
        }
        if (!Config.leaderboard.isSet(p.getPlayer().getUniqueId().toString() + ".losses")) {
            Config.leaderboard.set(p.getPlayer().getUniqueId().toString() + ".losses", 0);
        }
        Config.leaderboard.set(p.getPlayer().getUniqueId().toString() + ".username", p.getPlayer().getName());

        try {
            Config.leaderboard.save(Config.leaderboardFile);
        } catch (IOException ex) {
            Utils.getPlugin().getLogger().warning(ex.toString());
        }
    }
}
