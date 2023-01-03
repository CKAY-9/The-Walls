package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

class SinkHoleHandler {
    Player p;
    private int timer = Config.data.getInt("events.sinkHole.seconds");
    private int taskID = 0;
    private boolean sunk = false;
    private final ArrayList<Material> blocks = new ArrayList<>();

    public SinkHoleHandler(Player p) {
        this.p = p;
        p.sendMessage(Utils.formatText("&0&lSink Hole&r&c opening in " + timer + "s!"));
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 255, 1);

        ArmorStand stand = (ArmorStand) World.world.spawnEntity(p.getLocation().subtract(0, 1, 0), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);

        int size = Config.data.getInt("events.sinkHole.size");
        Location playerLoc = p.getLocation();

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), () -> {

            stand.setCustomNameVisible(true);
            stand.setCustomName(Utils.formatText("&0&lSink Hole &r- &c&l" + timer + "s"));

            if (timer <= 0 && !sunk) {
                stand.remove();
                int i = 0;
                for (int x = -size; x < (size + 1); x++) {
                    for (int z = -size; z < (size + 1); z++) {
                        for (int y = -64; y < 325; y++) {
                            if (World.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z).getType() == Material.AIR) continue;
                            blocks.add(i, World.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z).getType());
                            World.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z).setType(Material.AIR);
                            i++;
                        }
                    }
                }
                sunk = true;
            }

            if (timer <= -Config.data.getInt("events.sinkHole.timeUntilReset") && sunk && blocks.size() >= 1) {
                int i = 0;
                for (int x = -size; x < (size + 1); x++) {
                    for (int z = -size; z < (size + 1); z++) {
                        for (int y = -64; y < 325; y++) {
                            World.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z).setType(blocks.get(i));
                            i++;
                        }
                    }
                }
                Bukkit.getScheduler().cancelTask(taskID);
            }

            timer--;
        }, 0L, 20L);
    }
}

public class SinkHole extends Event {

    public SinkHole(String eventName) {
        super(eventName);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!Utils.isAlive(p)) continue;
            new SinkHoleHandler(p);
        }
    }
}
