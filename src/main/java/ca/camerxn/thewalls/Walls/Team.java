package ca.camerxn.thewalls.Walls;

import ca.camerxn.thewalls.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Team {
    public ArrayList<Player> members = new ArrayList<>();
    public int team; // 0 = red, 1 = blue, 2 = yellow, 3 = green
    public String teamName = "TBD";
    public String teamColor = "&f";
    public Location teamSpawn;
    public boolean alive = false;

    public Team(int _team, boolean overrideAlive) {
        team = _team;
        switch (team) {
            case 0:
                teamName = "Red";
                teamColor = "&c";
                int x0 = World.positionOne[0] - (Game.size / 2);
                int z0 = World.positionOne[1] - (Game.size / 2);
                int y0 = World.world.getHighestBlockYAt(x0, z0);
                teamSpawn = new Location(World.world, x0, y0, z0);
                break;
            case 1:
                teamName = "Blue";
                teamColor = "&9";
                int x1 = World.positionTwo[0] + (Game.size / 2);
                int z1 = World.positionTwo[1] + (Game.size / 2);
                int y1 = World.world.getHighestBlockYAt(x1, z1);
                teamSpawn = new Location(World.world, x1, y1, z1);
                break;
            case 2:
                teamName = "Yellow";
                teamColor = "&e";
                int x2 = (int) (World.positionOne[0] - (Game.size / 2));
                int z2 = (int) (World.positionOne[1] - (Game.size * 1.5));
                int y2 = World.world.getHighestBlockYAt(x2, z2);
                teamSpawn = new Location(World.world, x2, y2, z2);
                break;
            case 3:
                teamName = "Green";
                teamColor = "&2";
                int x3 = (int) (World.positionTwo[0] + (Game.size / 2));
                int z3 = (int) (World.positionTwo[1] + (Game.size * 1.5));
                int y3 = World.world.getHighestBlockYAt(x3, z3);
                teamSpawn = new Location(World.world, x3, y3, z3);
                break;
        }
        Game.teams.add(this);
        if (!overrideAlive) {
            alive = true;
            Game.aliveTeams.add(this);
        }
    }

    public void readyPlayers() {
        for (Player ply : members) {
            ply.setHealth(20);
            ply.setSaturation(20);
            ply.getInventory().clear();
            ply.sendTitle(Utils.formatText("&6&lThe Walls"), Utils.formatText("&cLast Team Standing Wins!"), 10, 80, 20);
            ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 255, 1);
            ply.setStatistic(Statistic.DEATHS, 0);
            ply.setDisplayName(Utils.formatText(teamColor + "[" + teamName + "] " + ply.getName()));
            ply.setPlayerListName(Utils.formatText(teamColor + "[" + teamName + "] " + ply.getName()));
            ply.setGameMode(GameMode.SURVIVAL);
            ply.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
            ply.teleport(teamSpawn);
        }
    }

    public static Team getPlayerTeam(Player p) {
        for (Team team : Game.teams) {
            for (Player ply : team.members) {
                if (p == ply) {
                    return team;
                }
            }
        }

        return null;
    }

}
