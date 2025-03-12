package ca.thewalls.Walls;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;
import ca.thewalls.Events.*;

import javax.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

class Loop implements Runnable {
    TheWalls walls;
    Game game;
    public Loop(TheWalls walls) {
        this.walls = walls;
        this.game = this.walls.game;
    }

    @Override
    public void run() {
        if (!game.started) return;

        if ((game.time >= game.prepTime) && !game.wallsFallen) {
            this.walls.world.dropWalls();
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 255, 1);
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 255, 1);
                p.sendMessage(Utils.formatText("&aThe Walls have fallen!"));
                p.sendTitle(Utils.formatText("&aThe Walls have fallen!"), "", 10, 80, 10);
            }
            game.wallsFallen = true;
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        for (Player p : Bukkit.getOnlinePlayers()) {
            assert manager != null;
            Scoreboard board = manager.getNewScoreboard();
            Objective obj = board.registerNewObjective("TheWalls-Primary", "dummy", Utils.formatText("&6&lThe Walls"));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score border = obj.getScore(Utils.formatText("&6=-=-=-=-=-=-=-=-=-=-="));
            border.setScore(99);

            Score timeRemaining;
            if (game.wallsFallen) {
                timeRemaining = obj.getScore(Utils.formatText("&c&lTHE WALLS ARE DOWN!"));
                if (game.eventTimer <= 0) {
                    // Run an event
                    if (Events.events.size() >= 1) {
                        int rnd = new Random().nextInt(Events.events.size());
                        Events.events.get(rnd).run();

                        // Reset timer
                        game.eventTimer = game.eventCooldown;
                    }
                }
                if (Events.events.size() >= 1) {
                    Score timeUntilEvent = obj.getScore(Utils.formatText("&6Event in " + game.eventTimer + "s"));
                    timeUntilEvent.setScore(97);
                }

                Score timeUntilBorderClose;
                if (game.borderCloseTimer <= 0) {
                    // Border are closing
                    timeUntilBorderClose = obj.getScore(Utils.formatText("&c&lBORDERS CLOSING IN"));
                    if (!game.borderClosing) {
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 255, 1);
                        p.sendMessage(Utils.formatText("&c&lThe Borders are closing in!"));
                        this.walls.world.world.getWorldBorder().setSize((double) game.size * Config.data.getDouble("world.borderShrinkPercentageOfSize"), game.borderCloseSpeed);
                        game.borderClosing = true;
                    }
                } else {
                    timeUntilBorderClose = obj.getScore(Utils.formatText("&6Borders closing in " + game.borderCloseTimer + "s"));
                }
                timeUntilBorderClose.setScore(96);
            } else {
                timeRemaining = obj.getScore(Utils.formatText("&6Preparation Time: " + (game.prepTime - game.time) + "s"));
            }
            timeRemaining.setScore(98);

            // Loop through teams
            Score teamsTitle = obj.getScore(Utils.formatText("&b&l=-=-=- TEAMS -=-=-="));
            teamsTitle.setScore(80);

            int currScore = 79;
            for (Team t : game.teams) {
                Score teamName;
                if (t.alive) {
                    if (Config.data.getBoolean("theWalls.legacyHud")) {
                        teamName = obj.getScore(Utils.formatText(t.teamColor + "&l" + t.teamName + "&r - &2ALIVE"));
                    } else {
                        teamName = obj.getScore(Utils.formatText(t.teamColor + "&l" + t.teamName + "&r" + t.teamColor + " - " + t.getAliveMembers() + " Alive"));
                    }
                } else {
                    teamName = obj.getScore(Utils.formatText(t.teamColor + "&l" + t.teamName + "&r - &cDEAD"));
                }
                teamName.setScore(currScore);
                currScore--;
                    // Loop through team members
                for (Player member : t.members) {
                    if (member == null) continue;

                    if (Config.data.getBoolean("theWalls.legacyHud")) {
                        Score tMember;
                        if (Utils.isAlive(member)) {
                            tMember = obj.getScore(Utils.formatText(t.teamColor + " - " + member.getName() + "&r - &2ALIVE"));
                        } else {
                            tMember = obj.getScore(Utils.formatText(t.teamColor + " - " + member.getName() + "&r - &cDEAD"));
                        }
                        tMember.setScore(currScore);
                        currScore--;
                    }
                }
            }

            if (!Utils.isAlive(p)) {
                Team tempTeam = Team.getPlayerTeam(p, game.teams);
                if ((Config.data.getBoolean("theWalls.respawnDuringPrepTime") && !game.wallsFallen) || (Config.data.getBoolean("theWalls.respawnDuringInitialFighting") && !game.borderClosing)) {
                    if (tempTeam != null) {
                        tempTeam.readyPlayer(p);
                    } else {
                        game.aliveTeams.get(0).members.add(p);
                        game.aliveTeams.get(0).readyPlayer(p);
                    }
                }
            }

            p.setScoreboard(board);
        }

        game.time++;
        if (game.wallsFallen) {
            if (Events.events.size() >= 1) {
                game.eventTimer--;
            }
            game.borderCloseTimer--;
        }
        if (Config.data.getBoolean("teams.checkWinEverySecond")) {
            this.walls.utils.checkWinner();
        }
    }
}

public class Game {

    TheWalls walls;

    public Game(TheWalls walls) {
        this.walls = walls;
    }

    public boolean started = false;
    public boolean wallsFallen = false;
    public boolean borderClosing = false;
    public int prepTime = 0;
    public int borderCloseTime = 0;
    public int borderCloseTimer = 0;
    public int borderCloseSpeed = 0;
    public int size = 0;
    public int eventTimer = 0;
    public int eventCooldown = 0;
    // All teams in the game
    public ArrayList<Team> teams = new ArrayList<>();
    // Used to determine the winning team of the walls
    public ArrayList<Team> aliveTeams = new ArrayList<>();
    public int gameLoopID = 0;

    public int time = 0;

    public void start(@Nullable Player starter) {

        if (Bukkit.getOnlinePlayers().size() == 0) {
            if (starter != null) {
                starter.sendMessage(Utils.formatText("&cThere are no online players!"));
            }
            return;
        }

        if (starter == null) {
            this.walls.world.world = Bukkit.getWorld(Objects.requireNonNull(Config.data.getString("theWalls.autoExecute.worldName")));
            assert this.walls.world.world != null;
            Location loc = new Location(this.walls.world.world, Config.data.getDouble("theWalls.autoExecute.center.x"), 0, Config.data.getDouble("theWalls.autoExecute.center.z"));
            this.walls.world.world.getWorldBorder().setCenter(loc.getX(), loc.getZ());
            this.walls.world.positionOne[0] = loc.getBlockX() + size;
            this.walls.world.positionOne[1] = loc.getBlockZ() + size;
            this.walls.world.positionTwo[0] = loc.getBlockX() - size;
            this.walls.world.positionTwo[1] = loc.getBlockZ() - size;
        } else {
            this.walls.world.world = starter.getWorld();
            this.walls.world.world.getWorldBorder().setCenter(starter.getLocation());
            this.walls.world.positionOne[0] = starter.getLocation().getBlockX() + size;
            this.walls.world.positionOne[1] = starter.getLocation().getBlockZ() + size;
            this.walls.world.positionTwo[0] = starter.getLocation().getBlockX() - size;
            this.walls.world.positionTwo[1] = starter.getLocation().getBlockZ() - size;
        }

        this.walls.world.world.getWorldBorder().setSize((size * 2) - 2);
        this.walls.world.world.setTime(1000);

        // Handle teams
        if (Bukkit.getOnlinePlayers().size() >= 1) {
            new Team(0, false, this.walls);
        }
        if (Bukkit.getOnlinePlayers().size() >= 2) {
            new Team(1, false, this.walls);
        }
        if (Bukkit.getOnlinePlayers().size() >= 3) {
            new Team(2, false, this.walls);
        }
        if (Bukkit.getOnlinePlayers().size() >= 4) {
            new Team(3, false, this.walls);
        }

        this.walls.world.save();
        this.walls.world.wallBlocks();
        
        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            teams.get(i % 4).members.add(p);
            teams.get(i % 4).readyPlayer(p);
            i++;
        }

        // Register events
        if (Config.data.getBoolean("events.tnt.enabled"))
            new TNTSpawn("TNT Spawn", this.walls);
        if (Config.data.getBoolean("events.blindSnail.enabled"))
            new BlindSnail("Blind Snail", this.walls);
        if (Config.data.getBoolean("events.locationSwap.enabled"))
            new LocationSwap("Player Location Swap", this.walls);
        if (Config.data.getBoolean("events.supplyChest.enabled"))
            new SupplyChest("Supply Chest", this.walls);
        if (Config.data.getBoolean("events.gregs.enabled"))
            new FreeFood("Free Food / Chicken Explosion", this.walls);
        if (Config.data.getBoolean("events.reveal.enabled"))
            new LocationReveal("Location Reveal", this.walls);
        if (Config.data.getBoolean("events.sinkHole.enabled"))
            new SinkHole("Sink Hole", this.walls);
        if (Config.data.getBoolean("events.hailStorm.enabled"))
            new HailStorm("Hail of Arrows", this.walls);
        if (Config.data.getBoolean("events.bossMan.enabled"))
            new BossMan("Boss Man", this.walls);
        if (Config.data.getBoolean("events.itemCheck.enabled"))
            new ItemCheck("Item Check", this.walls);
        if (Config.data.getBoolean("events.bombingRun.enabled"))
            new BombingRun("Bombing Run", this.walls);

        started = true;
        time = 0;
        eventTimer = eventCooldown;
        borderCloseTimer = borderCloseTime;
        gameLoopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), new Loop(walls), 20L, 20L);

        Utils.getPlugin().getLogger().info("Started game with teams: ");
        for (Team t : aliveTeams) {
            Utils.getPlugin().getLogger().info(" - " + t.teamName);
        }
    }

    // End the game and clean up anything related to it
    public void end(boolean forced, @Nullable Team winningTeam) {
        borderClosing = false;
        wallsFallen = false;
        started = false;

        Bukkit.getScheduler().cancelTask(gameLoopID);
        if (Utils.getPlugin().isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getPlugin(), this.walls.world::reset, 20*7);
        } else {
            this.walls.world.reset();
        }

        if (!forced) {
            for (Team t : teams) {
                for (Player p : t.members) {
                    if (t.alive) {
                        int wins = Config.leaderboard.getInt(p.getUniqueId().toString() + ".wins") + 1;
                        Config.leaderboard.set(p.getUniqueId().toString() + ".wins", wins);
                    } else {
                        int losses = Config.leaderboard.getInt(p.getUniqueId().toString() + ".losses") + 1;
                        Config.leaderboard.set(p.getUniqueId().toString() + ".losses", losses);
                    }
                }
            }

            try {
                Config.leaderboard.save(Config.leaderboardFile);
            } catch (IOException ex) {
                Utils.getPlugin().getLogger().warning(ex.toString());
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!forced && winningTeam != null) {
                p.sendTitle(Utils.formatText(winningTeam.teamColor + "&l" + winningTeam.teamName + " Team"), Utils.formatText(winningTeam.teamColor + "IS THE WINNER!"), 10, 80, 20);
            }
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.sendMessage(Utils.formatText("&aThe Walls has ended!"));
            p.setDisplayName(p.getName());
            p.getInventory().clear();
            p.setPlayerListName(p.getName());
            p.setGameMode(GameMode.SURVIVAL);
        }
        Events.events.clear();
        teams.clear();
        aliveTeams.clear();
    }
}
