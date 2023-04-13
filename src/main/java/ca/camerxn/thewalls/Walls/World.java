package ca.camerxn.thewalls.Walls;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class World {

    public static org.bukkit.World world;

    public static int[] positionOne = new int[2];
    public static int[] positionTwo = new int[2];

    public static ArrayList<TempBlock> originalBlocks = new ArrayList<>();
    public static ArrayList<TempBlock> originalWallBlocks = new ArrayList<>();

    // This method does take a while if the map size if >50;
    public static void save() {
        for (Entity ent : world.getEntities()) {
            if (ent.getType() == EntityType.DROPPED_ITEM) {
                ent.remove();
            }
        }

        if (!Config.data.getBoolean("world.saving")) return;

        Utils.getPlugin().getLogger().info("Saving original world data...");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&eSaving original world data..."));
            }
        }

        // Constant (10) is for safety with TNT explosions and other things.
        for (int x = positionTwo[0] - 10; x < positionOne[0] + 10; x++) {
            for (int z = positionTwo[1] - 10; z < positionOne[1] + 10; z++) {
                for (int y = -64; y < 325; y++) {
                    originalBlocks.add(new TempBlock(world.getBlockAt(x, y, z).getLocation(), world.getBlockAt(x, y, z).getType()));
                }
            }
        }
        Utils.getPlugin().getLogger().info("Saved original world data!");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&2Saved original world data!"));
            }
        }
    }

    // Replace all the blocks in the world with the originals
    public static void reset() {
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(29999980);
        world.getWorldBorder().setDamageAmount(0);
        
        // Clear items from the ground
        for (Entity ent : world.getEntities()) {
            if (ent.getType() == EntityType.DROPPED_ITEM) {
                ent.remove();
            }
        }

        if (!Config.data.getBoolean("world.saving")) return;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&eResetting world to original state..."));
            }
        }

        for (TempBlock oBlock : originalBlocks) {
            world.getBlockAt(oBlock.loc).setType(oBlock.block);
        }
        for (TempBlock wBlock : originalWallBlocks) {
            world.getBlockAt(wBlock.loc).setType(wBlock.block);
        }

        originalWallBlocks.clear();
        originalBlocks.clear();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&2Reset world to original state!"));
            }
        }

        world.getWorldBorder().setDamageAmount(0.2);
    }

    // Save and spawn (bedrock) blocks for the actual walls
    public static void wallBlocks() {
        Utils.getPlugin().getLogger().info("Saving original wall blocks...");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&eSetting up walls and saving data..."));
            }
        }

        for (int x = positionTwo[0]; x < positionOne[0]; x++) {
            for (int y = -64; y < 325; y++) {
                if (world.getBlockAt(x, y, positionOne[1] - Game.size).getType() == Material.BEDROCK) continue;
                originalWallBlocks.add(new TempBlock(world.getBlockAt(x, y, positionOne[1] - Game.size).getLocation(), world.getBlockAt(x, y, positionOne[1] - Game.size).getType()));
                world.getBlockAt(x, y, positionOne[1] - Game.size).setType(Material.BEDROCK);
            }
        }
        for (int z = positionTwo[1]; z < positionOne[1]; z++) {
            for (int y = -64; y < 325; y++) {
                if (world.getBlockAt(positionOne[0] - Game.size, y, z).getType() == Material.BEDROCK) continue;
                originalWallBlocks.add(new TempBlock(world.getBlockAt(positionOne[0] - Game.size, y, z).getLocation(), world.getBlockAt(positionOne[0] - Game.size, y, z).getType()));
                world.getBlockAt(positionOne[0] - Game.size, y, z).setType(Material.BEDROCK);
            }
        }

        Utils.getPlugin().getLogger().info("Saved original wall blocks!");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&2Walls are up and saved original block data!"));
            }
        }
    }

    public static void dropWalls() {
        for (TempBlock wallBlock: originalWallBlocks) {
            world.getBlockAt(wallBlock.loc).setType(wallBlock.block);
        }
    }

}
