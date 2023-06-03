package ca.camerxn.thewalls.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Team;

class BossManRunn implements Runnable {
    BossManHandler handler;

            
    public BossManRunn(BossManHandler handler) {
        this.handler = handler;
    }
    
    public void run() {
        if (!this.handler.walls.game.started) {
            handler.boss.setHealth(0);
            Bukkit.getServer().getScheduler().cancelTask(handler.taskID);
            return;
        }
        if (handler.boss.isDead()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(handler.ply.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 255, 1);
                p.sendMessage(Utils.formatText("&l" + Team.getPlayerTeam(p, this.handler.walls.game.teams).teamColor + p.getName() + "&r&5 has slain a " + Config.data.getString("events.bossMan.name") + "!"));
            }
            Bukkit.getServer().getScheduler().cancelTask(handler.taskID);
        } else {
            handler.boss.setCustomName(Utils.formatText("&c&l" + Config.data.getString("events.bossMan.name") + ": " + Math.round(handler.boss.getHealth()) + " HP"));
            handler.boss.setTarget(handler.ply);
        }
    }
}

class BossManHandler {
    Player ply;
    Zombie boss;
    int taskID;
    TheWalls walls;

    public BossManHandler(Player p, TheWalls walls) {
        this.walls = walls;
        this.ply = p;
        ply.playSound(ply.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 255, 1);
        ply.sendMessage(Utils.formatText("&c&l" + Config.data.getString("events.bossMan.name") + " is about to spawn! You have " + Config.data.getInt("events.bossMan.prepTime") + "s to prepare!"));
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Utils.getPlugin(), new Runnable() {
            public void run() {
                boss = (Zombie) p.getWorld().spawnEntity(p.getLocation().add(Math.random() * 2, Math.random() * 2, Math.random() * 2), EntityType.ZOMBIE, false);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 3, false));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, Config.data.getInt("events.bossMan.resistance")));
                boss.setTarget(ply);
                boss.setAdult();
                boss.setRemoveWhenFarAway(false);
                boss.getEquipment().clear();

                // Create items
                ItemStack helm = new ItemStack(Material.DIAMOND_HELMET, 1);
                helm.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Config.data.getInt("events.bossMan.protectionLevel"));
                helm.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Config.data.getInt("events.bossMan.protectionLevel"));
                chest.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                pants.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Config.data.getInt("events.bossMan.protectionLevel"));
                pants.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
                boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, Config.data.getInt("events.bossMan.protectionLevel"));
                boots.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                sword.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
                sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
                sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);

                // Equip
                boss.getEquipment().setBoots(boots);
                boss.getEquipment().setLeggings(pants);
                boss.getEquipment().setChestplate(chest);
                boss.getEquipment().setHelmet(helm);
                boss.getEquipment().setItemInMainHand(sword);

                // Drop rates
                boss.getEquipment().setBootsDropChance((float)Config.data.getDouble("events.bossMan.dropRate"));
                boss.getEquipment().setChestplateDropChance((float)Config.data.getDouble("events.bossMan.dropRate"));
                boss.getEquipment().setLeggingsDropChance((float)Config.data.getDouble("events.bossMan.dropRate"));
                boss.getEquipment().setBootsDropChance((float)Config.data.getDouble("events.bossMan.dropRate"));
            }
        }, Config.data.getLong("events.bossMan.prepTime") * 20L);

        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), new BossManRunn(this), Config.data.getLong("events.bossMan.prepTime") * 20L, 10L);
    }
}

public class BossMan extends Event {

    public BossMan(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p)) continue;
            new BossManHandler(p, this.walls);
        }
    }
    
}