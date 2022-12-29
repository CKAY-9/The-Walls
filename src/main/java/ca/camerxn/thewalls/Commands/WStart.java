package ca.camerxn.thewalls.Commands;

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

        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.formatText("&cYou need to be a player in the world to run this command!"));
            return false;
        }

        if (Game.started) {
            sender.sendMessage(Utils.formatText("&cThere is already an ongoing game of The Walls!"));
            return false;
        }

        Player ply = (Player) sender;

        try {
            Game.size = Integer.parseInt(args[0]);
            Game.prepTime = Integer.parseInt(args[1]);
            Game.borderCloseTime = Integer.parseInt(args[2]);
            Game.borderCloseSpeed = Integer.parseInt(args[3]);
            Game.eventCooldown = Integer.parseInt(args[4]);
            Game.start(ply);
        } catch (Exception ex) {
            ply.sendMessage(Utils.formatText("&cArguments provided aren't valid!"));
            Utils.getPlugin().getLogger().warning(ex.toString());
        }


        return false;
    }
}
