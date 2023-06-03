package ca.camerxn.thewalls.Commands;

import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WEnd implements CommandExecutor {
    public TheWalls walls;
    public WEnd(TheWalls walls) {
        this.walls = walls;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Utils.formatText("&cYou don't have the permissions to run this command!"));
            return false;
        }
        if (!this.walls.game.started) {
            sender.sendMessage(Utils.formatText("&cThere is no game currently going on!"));
            return false;
        }

        this.walls.game.end(true, null);
        return false;
    }
}
