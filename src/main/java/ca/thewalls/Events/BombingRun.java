package ca.thewalls.Events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

class BombingRunHandler {
    public TheWalls walls;

    public BombingRunHandler(TheWalls walls) {
        this.walls = walls;
        Random rand = new Random();

        // Alert players
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Utils.formatText("&c&lBOMBING RUN INCOMING!!!"));
            p.playSound(p.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 255f, 0.5f);
        }

        // Get the final z coordinates for the run
        int[] zPoints = {
                this.walls.world.positionTwo[1],
                this.walls.world.positionOne[1]
        };

        int tntSpread = Config.data.getInt("events.bombingRun.tntSpread", 2);
        int totalDifference = Math.abs(zPoints[0] - zPoints[1]);
        int totalIterations = totalDifference / tntSpread;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (!walls.game.started) {
                    return;
                }

                // X Pos for TNT
                int furthestX = walls.world.positionOne[0];
                int closestX = walls.world.positionTwo[0];

                for (Player p : Bukkit.getOnlinePlayers()) {
                    Location loc = p.getLocation();
                    int x = loc.getBlockX();

                    if (Math.abs(x) > Math.abs(furthestX)) {
                        furthestX = x;
                    }
                    if (Math.abs(x) < Math.abs(closestX)) {
                        closestX = x;
                    }
                }

                final int xPos;

                if (closestX > furthestX) {
                    xPos = rand.nextInt(furthestX, closestX);
                } else {
                    xPos = rand.nextInt(closestX, furthestX);
                }

                int timer = Config.data.getInt("events.bombingRun.detonationtime", 10);
                int power = Config.data.getInt("events.bombingRun.tntPower",  16);

                for (int i = 0; i < totalIterations; i++) {
                    Location loc = new Location(walls.world.world, xPos, 325, zPoints[0] + ((i + 1) * tntSpread));
                    TNTPrimed tnt = (TNTPrimed) walls.world.world.spawnEntity(loc, EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(20 * (timer + 2));

                    if (!walls.game.started) {
                        tnt.teleport(new Location(walls.world.world, 20_000_000, 600, 20_000_000));
                        tnt.remove();
                        return;
                    }

                    final int iter = i;

                    Bukkit.getScheduler().scheduleSyncDelayedTask(walls, new Runnable() {
                        @Override
                        public void run() {
                            if (!walls.game.started) {
                                return;
                            }

                            Block highestBlock = walls.world.world.getHighestBlockAt(xPos, zPoints[0] + ((iter + 1) * tntSpread));
                            Location highestLoc = highestBlock.getLocation();
                            walls.world.world.createExplosion(highestLoc, power, true, true);
                            tnt.teleport(new Location(walls.world.world, 20_000_000, 600, 20_000_000));
                            tnt.remove();
                        };
                    }, 20 * timer);
                }
            }
        }, 20 * Config.data.getInt("events.bombingRun.alertTime", 5));
    }
}

public class BombingRun extends Event {
    public BombingRun(String eventName, TheWalls walls) {
        super(eventName, walls);
    }

    @Override
    public void run() {
        new BombingRunHandler(this.walls);
    }
}
