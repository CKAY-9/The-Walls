package ca.camerxn.thewalls.Walls;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Team {
    public ArrayList<Player> members = new ArrayList<>();
    public int team; // 0 = red, 1 = blue, 2 = yellow, 3 = green
    public String teamName = "TBD";
    public String teamColor = "&f";
    public Location teamSpawn = new Location(World.world, 0, 0, 0);
    public boolean alive = false;

    public Team(int _team, boolean overrideAlive) {
        team = _team;
        switch (team) {
            case 0:
                teamName = Config.data.getString("teams.zero.name");
                teamColor = Config.data.getString("teams.zero.color");
                int x0 = World.positionOne[0] - (Game.size / 2);
                int z0 = World.positionOne[1] - (Game.size / 2);
                int y0 = World.world.getHighestBlockYAt(x0, z0);
                teamSpawn = new Location(World.world, x0, y0, z0);
                break;
            case 1:
                teamName = Config.data.getString("teams.one.name");
                teamColor = Config.data.getString("teams.one.color");
                int x1 = World.positionTwo[0] + (Game.size / 2);
                int z1 = World.positionTwo[1] + (Game.size / 2);
                int y1 = World.world.getHighestBlockYAt(x1, z1);
                teamSpawn = new Location(World.world, x1, y1, z1);
                break;
            case 2:
                teamName = Config.data.getString("teams.two.name");
                teamColor = Config.data.getString("teams.two.color");
                int x2 = (int) (World.positionOne[0] - (Game.size / 2));
                int z2 = (int) (World.positionOne[1] - (Game.size * 1.5));
                int y2 = World.world.getHighestBlockYAt(x2, z2);
                teamSpawn = new Location(World.world, x2, y2, z2);
                break;
            case 3:
                teamName = Config.data.getString("teams.three.name");
                teamColor = Config.data.getString("teams.three.color");
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

    public int getAliveMembers() {
        int count = 0;
        for (Player ply : members) {
            if (Utils.isAlive(ply)) {
                count++;
            }
        }
        return count;
    }

    public void readyPlayer(Player ply) {
        ply.setHealth(20);
        ply.setSaturation(20);
        ply.getInventory().clear();
        ply.sendTitle(Utils.formatText("&6&lThe Walls"), Utils.formatText("&cLast Team Standing Wins!"), 10, 80, 20);
        ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 255, 1);
        ply.setStatistic(Statistic.DEATHS, 0);
        ply.setDisplayName(Utils.formatText(teamColor + "[" + teamName + "] " + ply.getName()));
        ply.setPlayerListName(Utils.formatText(teamColor + "[" + teamName + "] " + ply.getName()));
        ply.setGameMode(GameMode.SURVIVAL);
        ply.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, Config.data.getInt("players.spawn.steakAmount")));

        // Calc teamspawn in case of change
        teamSpawn.setY(World.world.getHighestBlockYAt(teamSpawn.getBlockX(), teamSpawn.getBlockZ()));
        ply.teleport(teamSpawn.add(0, 2, 0));
    }

    public void readyPlayers() {
        for (Player ply : members) {
            readyPlayer(ply);
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
