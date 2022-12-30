package ca.camerxn.thewalls.Commands;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WForceTeam implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Utils.formatText("&cYou don't have the permissions to run this command!"));
            return false;
        }
        if (!Game.started) {
            sender.sendMessage(Utils.formatText("&cThere is no game currently going on!"));
            return false;
        }
        if (Game.borderClosing) {
            sender.sendMessage(Utils.formatText("&cThis command cannot be performed when the borders are closing!"));
            return false;
        }

        try {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Utils.formatText("&cCouldn't find player with the name of " + args[0] + "!"));
                return false;
            }
            String newTeam = args[1].toLowerCase();
            boolean flag = false;

            for (int i = 0; i < Game.teams.size(); i++) {
                Team temp = Game.teams.get(i);
                if (temp.teamName.toLowerCase().equalsIgnoreCase(newTeam)) {
                    Team prev = Team.getPlayerTeam(target);

                    if (!temp.alive) {
                        temp.alive = true;
                        Game.aliveTeams.add(temp);
                    }

                    temp.members.add(target);
                    target.teleport(temp.teamSpawn);
                    target.sendMessage(Utils.formatText(temp.teamColor + "You have been swapped to " + temp.teamName + " team!"));

                    if (prev != null) {
                        prev.members.remove(target);
                        if (prev.members.size() == 0) {
                            Game.aliveTeams.remove(prev);
                            Game.teams.remove(prev);
                        }
                    }

                    flag = true;
                    break;
                }
            }
            // create relevant team;
            if (!flag) {
                Team prev = Team.getPlayerTeam(target);

                int teamID = 0;
                if (newTeam.equalsIgnoreCase("blue")) {
                    teamID = 1;
                }
                if (newTeam.equalsIgnoreCase("yellow")) {
                    teamID = 2;
                }
                if (newTeam.equalsIgnoreCase("green")) {
                    teamID = 3;
                }

                Team newT = new Team(teamID, false);
                newT.members.add(target);
                target.teleport(newT.teamSpawn);
                target.sendMessage(Utils.formatText(newT.teamColor + "You have been swapped to " + newT.teamName + " team!"));

                if (prev != null) {
                    prev.members.remove(target);
                    if (prev.members.size() == 0) {
                        Game.aliveTeams.remove(prev);
                        Game.teams.remove(prev);
                    }
                }
            }

            Team finalTeam = Team.getPlayerTeam(target);
            if (finalTeam != null) {
                target.setDisplayName(Utils.formatText(finalTeam.teamColor + "[" + finalTeam.teamName + "] " + target.getName()));
                target.setPlayerListName(Utils.formatText(finalTeam.teamColor + "[" + finalTeam.teamName + "] " + target.getName()));
                target.getInventory().clear();
                target.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
                if (!Utils.isAlive(target)) {
                    target.spigot().respawn();
                    target.setGameMode(GameMode.SURVIVAL);
                }
            }
        } catch (Exception e) {
            sender.sendMessage(Utils.formatText("&cArguments provided aren't valid!"));
            Utils.getPlugin().getLogger().warning(e.toString());
        }

        return false;
    }
}
