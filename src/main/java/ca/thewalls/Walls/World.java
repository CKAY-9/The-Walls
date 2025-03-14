package ca.thewalls.Walls;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

import java.util.ArrayList;

public class World {

    public TheWalls walls;
    public org.bukkit.World world;

    /*
    *   positionOne is always the "largest" coordinate, as in worldSize gets added to the origin
    *   positionTwo is always the "smallest" coordinate, with worldSize being subtracted from the origin
    *   Example: Origin: (x: 0, z: 0) then
    *       positionOne: (+worldSize, +worldSize)
    *       positionTwo: (-worldSize, -worldSize)
    */
    public int[] positionOne = new int[2];
    public int[] positionTwo = new int[2];

    public ArrayList<TempBlock> originalBlocks = new ArrayList<>();
    public ArrayList<TempBlock> originalWallBlocks = new ArrayList<>();
    public ArrayList<Location> spawnProtectionBlocks = new ArrayList<>();
  
    public World(TheWalls walls) {
        this.walls = walls;
    }
    
    // This method does take a while if the map size if >50;
    public void save() {
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

        // Constant safetyBounds is for safety with TNT explosions and other things.
        int safetyBounds = Config.data.getInt("world.safetyBounds", 3);
        for (int x = positionTwo[0] - safetyBounds; x < positionOne[0] + safetyBounds; x++) {
            for (int z = positionTwo[1] - safetyBounds; z < positionOne[1] + safetyBounds; z++) {
                for (int y = -64; y < 325; y++) {
                    // This is to save memory and shorten load times, other files will deal with this if they need (ex: SupplyChest event)
                    // Update 1.3.1: Config setting to save air because issues with player placed blocks
                    if (world.getBlockAt(x, y, z).getType() == Material.AIR && !Config.data.getBoolean("world.saveAir", true)) {
                        continue;
                    }
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
    public void reset() {
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
        spawnProtectionBlocks.clear();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&2Reset world to original state!"));
            }
        }

        world.getWorldBorder().setDamageAmount(0.2);
    }
    // Save and spawn (bedrock) blocks for the actual walls
    public void wallBlocks() {
        Utils.getPlugin().getLogger().info("Saving original wall blocks...");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&eSetting up walls and saving data..."));
            }
        }

        for (int x = positionTwo[0]; x < positionOne[0]; x++) {
            for (int y = -64; y < 325; y++) {
                if (world.getBlockAt(x, y, positionOne[1] - this.walls.game.size).getType() == Material.BEDROCK) continue;
                originalWallBlocks.add(new TempBlock(world.getBlockAt(x, y, positionOne[1] - this.walls.game.size).getLocation(), world.getBlockAt(x, y, positionOne[1] - this.walls.game.size).getType()));
                world.getBlockAt(x, y, positionOne[1] - this.walls.game.size).setType(Material.BEDROCK);
            }
        }
        for (int z = positionTwo[1]; z < positionOne[1]; z++) {
            for (int y = -64; y < 325; y++) {
                if (world.getBlockAt(positionOne[0] - this.walls.game.size, y, z).getType() == Material.BEDROCK) continue;
                originalWallBlocks.add(new TempBlock(world.getBlockAt(positionOne[0] - this.walls.game.size, y, z).getLocation(), world.getBlockAt(positionOne[0] - this.walls.game.size, y, z).getType()));
                world.getBlockAt(positionOne[0] - this.walls.game.size, y, z).setType(Material.BEDROCK);
            }
        }

        Utils.getPlugin().getLogger().info("Saved original wall blocks!");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(Utils.adminMessage("&2Walls are up and saved original block data!"));
            }
        }
    }

    public void dropWalls() {
        for (TempBlock wallBlock: originalWallBlocks) {
            world.getBlockAt(wallBlock.loc).setType(wallBlock.block);
        }

        if (Config.data.getBoolean("teams.clearProtectionBlocksAfterDrop", true)) {
            for (Location loc : spawnProtectionBlocks) {
                // Remove protection blocks
                world.getBlockAt(loc).setType(Material.AIR);
            }
        }
    }

}
