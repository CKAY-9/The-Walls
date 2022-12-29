package ca.camerxn.thewalls.Walls;

import ca.camerxn.thewalls.Events.*;
import ca.camerxn.thewalls.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    public static boolean started = false;
    public static boolean wallsFallen = false;
    public static boolean borderClosing = false;
    public static int prepTime = 0;
    public static int borderCloseTime = 0;
    private static int borderCloseTimer = 0;
    public static int borderCloseSpeed = 0;
    public static int size = 0;
    private static int eventTimer = 0;
    public static int eventCooldown = 0;
    // All teams in the game
    public static ArrayList<Team> teams = new ArrayList<>();
    // Used to determine the winning team of the walls
    public static ArrayList<Team> aliveTeams = new ArrayList<>();
    public static int gameLoopID = 0;

    private static int time = 0;

    public static void start(Player starter) {
        if (Bukkit.getOnlinePlayers().size() == 0) {
            starter.sendMessage(Utils.formatText("&cThere are no online players!"));
            return;
        }

        World.world = starter.getWorld();
        World.world.getWorldBorder().setCenter(starter.getLocation());
        World.world.getWorldBorder().setSize((size * 2) - 2);
        World.positionOne[0] = starter.getLocation().getBlockX() + size;
        World.positionOne[1] = starter.getLocation().getBlockZ() + size;
        World.positionTwo[0] = starter.getLocation().getBlockX() - size;
        World.positionTwo[1] = starter.getLocation().getBlockZ() - size;

        World.save();
        World.wallBlocks();

        // Handle teams
        if (Bukkit.getOnlinePlayers().size() >= 1) {
            new Team(0, false);
        }
        if (Bukkit.getOnlinePlayers().size() >= 2) {
            new Team(1, false);
        }
        if (Bukkit.getOnlinePlayers().size() >= 3) {
            new Team(2, false);
        }
        if (Bukkit.getOnlinePlayers().size() >= 4) {
            new Team(3, false);
        }

        int i = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            teams.get(i % 4).members.add(p);
            i++;
        }
        for (Team team : aliveTeams) {
            team.readyPlayers();
        }

        // Register events
        new TNTSpawn("TNT Spawn");
        new BlindSnail("Blind Snail");
        new LocationSwap("Player Location Swap");
        new SupplyChest("Supply Chest");

        started = true;
        time = 0;
        eventTimer = eventCooldown;
        borderCloseTimer = borderCloseTime;
        gameLoopID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Utils.getPlugin(), Game::loop, 20L, 20L);

        Utils.getPlugin().getLogger().info("Started game with teams: ");
        for (Team t : aliveTeams) {
            Utils.getPlugin().getLogger().info(" - " + t.teamName);
        }
    }

    // End the game and clean up anything related to it
    public static void end() {
        World.reset();
        borderClosing = false;
        wallsFallen = false;
        Bukkit.getScheduler().cancelTask(gameLoopID);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.sendMessage(Utils.formatText("&aThe Walls has ended!"));
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());
            p.setGameMode(GameMode.SURVIVAL);
        }
        Events.events.clear();
        teams.clear();
        aliveTeams.clear();
        started = false;
    }

    // Primary game loop for the walls
    private static void loop() {
        if (!started) return;

        if ((time >= prepTime) && !wallsFallen) {
            World.dropWalls();
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 255, 1);
                p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 255, 1);
                p.sendMessage(Utils.formatText("&aThe Walls have fallen!"));
            }
            wallsFallen = true;
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
            if (wallsFallen) {
                timeRemaining = obj.getScore(Utils.formatText("&c&lTHE WALLS ARE DOWN!"));
                if (eventTimer <= 0) {
                    // Run an event
                    int rnd = new Random().nextInt(Events.events.size());
                    Events.events.get(rnd).run();

                    // Reset timer
                    eventTimer = eventCooldown;
                }
                // TODO: Implement border timer
                Score timeUntilEvent = obj.getScore(Utils.formatText("&6Event in " + eventTimer + "s"));
                timeUntilEvent.setScore(97);

                Score timeUntilBorderClose;
                if (borderCloseTimer <= 0) {
                    // Border are closing
                    timeUntilBorderClose = obj.getScore(Utils.formatText("&c&lBORDERS CLOSING IN"));

                    if (!borderClosing) {
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 255, 1);
                        p.sendMessage(Utils.formatText("&c&lThe Borders are closing in!"));
                        World.world.getWorldBorder().setSize((double) size / 8, borderCloseSpeed);
                        borderClosing = true;
                    }
                } else {
                    timeUntilBorderClose = obj.getScore(Utils.formatText("&6Borders closing in " + borderCloseTimer + "s"));
                }
                timeUntilBorderClose.setScore(96);
            } else {
                timeRemaining = obj.getScore(Utils.formatText("&6Preparation Time: " + (prepTime - time) + "s"));
            }
            timeRemaining.setScore(98);

            // Loop through teams
            Score teamsTitle = obj.getScore(Utils.formatText("&b&l=-=-=- TEAMS -=-=-="));
            teamsTitle.setScore(80);

            int currScore = 79;
            for (Team t : Game.teams) {
                Score teamName;
                if (t.alive) {
                    teamName = obj.getScore(Utils.formatText(t.teamColor + "&l" + t.teamName + "&r - &2ALIVE"));
                } else {
                    teamName = obj.getScore(Utils.formatText(t.teamColor + "&l" + t.teamName + "&r - &cDEAD"));
                }
                teamName.setScore(currScore);
                currScore--;
                // Loop through team members
                for (Player member : t.members) {
                    if (member == null) continue;
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

            p.setScoreboard(board);
        }
        time++;
        if (wallsFallen) {
            eventTimer--;
            borderCloseTimer--;
        }
    }

}
