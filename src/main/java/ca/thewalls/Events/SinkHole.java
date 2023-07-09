package ca.thewalls.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;
import ca.thewalls.Walls.TempBlock;

import java.util.ArrayList;

class SinkHoleHandler {
    Player p;
    private int timer = Config.data.getInt("events.sinkHole.seconds");
    private int taskID = 0;
    private boolean sunk = false;
    private final ArrayList<TempBlock> blocks = new ArrayList<>();
    TheWalls walls;

    public SinkHoleHandler(Player p, TheWalls walls) {
        this.p = p;
        this.walls = walls;

        ArmorStand stand = (ArmorStand) this.walls.world.world.spawnEntity(p.getLocation().subtract(0, 2, 0), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setSmall(true);
        stand.setHealth(1);

        int size = Config.data.getInt("events.sinkHole.size");
        Location playerLoc = p.getLocation();

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), () -> {

            stand.setCustomNameVisible(true);
            stand.setCustomName(Utils.formatText("&0&lSink Hole &r- &c&l" + timer + "s"));

            if (timer <= 0 && !sunk) {
                stand.remove();
                for (int x = -size; x < (size + 1); x++) {
                    for (int z = -size; z < (size + 1); z++) {
                        for (int y = -64; y < 325; y++) {
                            if (this.walls.world.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z).getType() == Material.AIR) continue;
                            Block temp = this.walls.world.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z);
                            blocks.add(new TempBlock(temp.getLocation(), temp.getType()));
                            this.walls.world.world.getBlockAt(playerLoc.getBlockX() + x, y, playerLoc.getBlockZ() + z).setType(Material.AIR);
                        }
                    }
                }
                sunk = true;
            }

            if (timer <= -Config.data.getInt("events.sinkHole.timeUntilReset") && sunk && blocks.size() >= 1) {
                for (TempBlock b : blocks) {
                    this.walls.world.world.getBlockAt(b.loc).setType(b.block);
                }
                Bukkit.getScheduler().cancelTask(taskID);
            }

            timer--;
        }, 0L, 20L);
    }
}

public class SinkHole extends Event {

    public SinkHole(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 255, 1);
            p.sendMessage(Utils.formatText("&0&lSink Hole&r&c opening in " + Config.data.getInt("events.sinkHole.seconds") + "s!"));
            if (!Utils.isAlive(p)) continue;
            new SinkHoleHandler(p, this.walls);
        }
    }
}
