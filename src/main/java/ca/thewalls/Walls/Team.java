package ca.thewalls.Walls;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;
import ca.thewalls.Commands.WEvents;

import java.util.ArrayList;

public class Team {
    public ArrayList<Player> members = new ArrayList<>();
    public int team; // 0 = red, 1 = blue, 2 = yellow, 3 = green
    public String teamName = "TBD";
    public String teamColor = "&f";
    public Location teamSpawn;
    public boolean alive = false;
    public TheWalls walls;

    public Team(int _team, boolean overrideAlive, TheWalls walls) {
        this.walls = walls;
        new Location(this.walls.world.world, 0, 0, 0);
        team = _team;
        switch (team) {
            case 0:
                teamName = Config.data.getString("teams.zero.name");
                teamColor = Config.data.getString("teams.zero.color");
                int x0 = this.walls.world.positionOne[0] - (this.walls.game.size / 2);
                int z0 = this.walls.world.positionOne[1] - (this.walls.game.size / 2);
                int y0 = this.walls.world.world.getHighestBlockYAt(x0, z0);
                teamSpawn = new Location(this.walls.world.world, x0, y0, z0);
                break;
            case 1:
                teamName = Config.data.getString("teams.one.name");
                teamColor = Config.data.getString("teams.one.color");
                int x1 = this.walls.world.positionTwo[0] + (this.walls.game.size / 2);
                int z1 = this.walls.world.positionTwo[1] + (this.walls.game.size / 2);
                int y1 = this.walls.world.world.getHighestBlockYAt(x1, z1);
                teamSpawn = new Location(this.walls.world.world, x1, y1, z1);
                break;
            case 2:
                teamName = Config.data.getString("teams.two.name");
                teamColor = Config.data.getString("teams.two.color");
                int x2 = (int) (this.walls.world.positionOne[0] - (this.walls.game.size / 2));
                int z2 = (int) (this.walls.world.positionOne[1] - (this.walls.game.size * 1.5));
                int y2 = this.walls.world.world.getHighestBlockYAt(x2, z2);
                teamSpawn = new Location(this.walls.world.world, x2, y2, z2);
                break;
            case 3:
                teamName = Config.data.getString("teams.three.name");
                teamColor = Config.data.getString("teams.three.color");
                int x3 = (int) (this.walls.world.positionTwo[0] + (this.walls.game.size / 2));
                int z3 = (int) (this.walls.world.positionTwo[1] + (this.walls.game.size * 1.5));
                int y3 = this.walls.world.world.getHighestBlockYAt(x3, z3);
                teamSpawn = new Location(this.walls.world.world, x3, y3, z3);
                break;
        }

        this.walls.game.teams.add(this);
        if (!overrideAlive) {
            alive = true;
            this.walls.game.aliveTeams.add(this);
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
        WEvents.annouceEvents(ply);
        ply.setHealth(20);
        ply.setSaturation(20);

        // Handle inventory
        ply.getInventory().clear();
        ply.getInventory().remove(ply.getItemOnCursor());
        ply.getInventory().remove(ply.getItemInUse());
        ply.setItemOnCursor(null);
        ply.updateInventory();

        // Noti
        ply.sendTitle(Utils.formatText("&6&lThe Walls"), Utils.formatText("&cLast Team Standing Wins!"), 10, 80, 20);
        ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 255, 1);

        // Game Stuff
        ply.setDisplayName(Utils.formatText(teamColor + "[" + teamName + "] " + ply.getName()));
        ply.setPlayerListName(Utils.formatText(teamColor + "[" + teamName + "] " + ply.getName()));
        ply.setGameMode(GameMode.SURVIVAL);
        ply.setStatistic(Statistic.DEATHS, 0);
        ply.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, Config.data.getInt("players.spawn.steakAmount")));

        // Calc teamspawn in case of change
        teamSpawn.setY(this.walls.world.world.getHighestBlockYAt(teamSpawn.getBlockX(), teamSpawn.getBlockZ()));
        ply.teleport(teamSpawn.add(0, 2, 0));
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                TempBlock temp = new TempBlock(new Location(this.walls.world.world, teamSpawn.getBlockX() + x, teamSpawn.getBlockY() - 1, teamSpawn.getBlockZ() + z),
                    this.walls.world.world.getBlockAt(teamSpawn.getBlockX() + x, teamSpawn.getBlockY() - 1, teamSpawn.getBlockZ() + z).getType());
                this.walls.world.originalBlocks.add(temp);
                if (Config.data.getBoolean("teams.clearProtectionBlocksAfterDrop", true)) {
                    this.walls.world.spawnProtectionBlocks.add(temp.loc);
                }
                this.walls.world.world.getBlockAt(temp.loc).setType(Material.BEDROCK);
            }
        }
    }

    public static Team getPlayerTeam(Player p, ArrayList<Team> teams) {
        for (Team team : teams) {
            for (Player ply : team.members) {
                if (p == ply) {
                    return team;
                }
            }
        }

        return null;
    }

}
