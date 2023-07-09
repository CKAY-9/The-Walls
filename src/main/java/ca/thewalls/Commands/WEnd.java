package ca.thewalls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

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
