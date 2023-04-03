package ca.camerxn.thewalls.Commands;

import ca.camerxn.thewalls.Utils;
import ca.camerxn.thewalls.Walls.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WEnd implements CommandExecutor {
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

        Game.end(true, null);
        return false;
    }
}
