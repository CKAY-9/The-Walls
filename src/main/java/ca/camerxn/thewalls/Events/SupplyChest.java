package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;

public class SupplyChest extends Event{

    ItemStack[] gearChest = new ItemStack[]{
            new ItemStack(Material.DIAMOND_AXE, 1),
            new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
            new ItemStack(Material.GOLDEN_APPLE, 3),
            new ItemStack(Material.IRON_INGOT, 32)
    };
    ItemStack[] griefChest = new ItemStack[]{
            new ItemStack(Material.TNT, 32),
            new ItemStack(Material.FLINT_AND_STEEL, 1),
            new ItemStack(Material.FLINT_AND_STEEL, 1),
            new ItemStack(Material.LAVA_BUCKET, 1),
            new ItemStack(Material.LAVA_BUCKET, 1),
            new ItemStack(Material.LAVA_BUCKET, 1),
            new ItemStack(Material.LAVA_BUCKET, 1)
    };
    ItemStack[] blocksChest = new ItemStack[]{
            new ItemStack(Material.OAK_LOG, 64),
            new ItemStack(Material.COBBLESTONE, 64),
            new ItemStack(Material.COBBLESTONE, 64),
            new ItemStack(Material.COBBLESTONE, 64),
            new ItemStack(Material.COBBLESTONE, 64),
            new ItemStack(Material.OBSIDIAN, 8)
    };
    ItemStack[] enchantChest = new ItemStack[]{
            new ItemStack(Material.ENCHANTING_TABLE, 1),
            new ItemStack(Material.BOOKSHELF, 22),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 64),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 64),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 64),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 64),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 64),
            new ItemStack(Material.EXPERIENCE_BOTTLE, 64),
            new ItemStack(Material.LAPIS_LAZULI, 64),
            new ItemStack(Material.LAPIS_LAZULI, 64),
            new ItemStack(Material.GRINDSTONE, 1),
            new ItemStack(Material.ANVIL, 1)
    };
    ArrayList<ItemStack[]> chests = new ArrayList<>();

    public SupplyChest(String eventName, TheWalls walls) {
        super(eventName, walls);
        chests.add(enchantChest);
        chests.add(gearChest);
        chests.add(griefChest);
        chests.add(blocksChest);
    }

    @Override
    public void run() {
        double reducer = 1 - Config.data.getDouble("events.supplyChest.allowedRegionPercentageOfSize");
        int[] positionOne = new int[]{(int) (this.walls.world.positionOne[0] - (this.walls.game.size * reducer)), (int) (this.walls.world.positionOne[1] - (this.walls.game.size * reducer))};
        int[] positionTwo = new int[]{(int) (this.walls.world.positionTwo[0] + (this.walls.game.size * reducer)), (int) (this.walls.world.positionTwo[1] + (this.walls.game.size * reducer))};

        Random rand = new Random();
        int randX = rand.nextInt(positionOne[0] - positionTwo[0]) + positionTwo[0];
        int randZ = rand.nextInt(positionOne[1] - positionTwo[1]) + positionTwo[1];
        int y = this.walls.world.world.getHighestBlockYAt(randX, randZ);

        Location chestLoc = new Location(this.walls.world.world, randX, y + 1, randZ);
        chestLoc.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) chestLoc.getBlock().getState();
        int chestInv = rand.nextInt(chests.size());
        chest.getInventory().setContents(chests.get(chestInv));

        Firework firework = (Firework) this.walls.world.world.spawnEntity(chestLoc.add(0, 4, 0), EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Builder fwb = FireworkEffect.builder();
        fwb.flicker(true);
        fwb.trail(true);
        fwb.with(Type.BALL_LARGE);
        fwb.withColor(Color.GREEN);
        fwb.withFade(Color.RED);
        meta.addEffect(fwb.build());
        firework.setFireworkMeta(meta);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 255, 1);
            p.playSound(chestLoc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 255, 1);
            p.sendMessage(Utils.formatText("&2A Supply Chest has spawned near the center of the map!"));
        }
    }
}
