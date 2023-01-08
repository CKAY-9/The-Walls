package ca.camerxn.thewalls.Commands;

import ca.camerxn.thewalls.Config;
import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Start command for The Walls
public class WStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Utils.formatText("&cYou don't have the permissions to run this command!"));
            return false;
        }

        if (!(sender instanceof Player) && !Config.data.isSet("theWalls.autoExecute.center")) {
            sender.sendMessage(Utils.formatText("&cYou need to be a player in the world to run this command or you can setup the default match config in config.yml!"));
            return false;
        }

        if (Game.started) {
            sender.sendMessage(Utils.formatText("&cThere is already an ongoing game of The Walls!"));
            return false;
        }

        try {

            Game.size = Config.data.getInt("theWalls.autoExecute.size");
            if (args.length >= 1) {
                Game.size = Integer.parseInt(args[0]);
            }
            Game.prepTime = Config.data.getInt("theWalls.autoExecute.prepTime");
            if (args.length >= 2) {
                Game.prepTime = Integer.parseInt(args[1]);
            }
            Game.borderCloseTime = Config.data.getInt("theWalls.autoExecute.timeUntilBorderClose");
            if (args.length >= 3) {
                Game.borderCloseTime = Integer.parseInt(args[2]);
            }
            Game.borderCloseSpeed = Config.data.getInt("theWalls.autoExecute.speedOfBorderClose");
            if (args.length >= 4) {
                Game.borderCloseSpeed = Integer.parseInt(args[3]);
            }
            Game.eventCooldown = Config.data.getInt("theWalls.autoExecute.eventCooldown");
            if (args.length >= 5) {
                Game.eventCooldown = Integer.parseInt(args[4]);
            }
            if (sender instanceof Player) {
                Game.start((Player) sender);
            } else {
                Game.start(null);
            }
        } catch (Exception ex) {
            sender.sendMessage(Utils.formatText("&cIssues starting The Walls!"));
            Utils.getPlugin().getLogger().warning(ex.toString());
        }


        return false;
    }
}
