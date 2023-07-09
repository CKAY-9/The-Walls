package ca.thewalls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

// Start command for The Walls
public class WStart implements CommandExecutor {
    public TheWalls walls;
    public WStart(TheWalls walls) {
        this.walls = walls;
    }

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

        if (this.walls.game.started) {
            sender.sendMessage(Utils.formatText("&cThere is already an ongoing game of The Walls!"));
            return false;
        }

        try {
            
            // In case the admin doesn't properly setup game values
            this.walls.game.size = Config.data.getInt("theWalls.autoExecute.size");
            if (args.length >= 1) {
                this.walls.game.size = Integer.parseInt(args[0]);
            }
            this.walls.game.prepTime = Config.data.getInt("theWalls.autoExecute.prepTime");
            if (args.length >= 2) {
                this.walls.game.prepTime = Integer.parseInt(args[1]);
            }
            this.walls.game.borderCloseTime = Config.data.getInt("theWalls.autoExecute.timeUntilBorderClose");
            if (args.length >= 3) {
                this.walls.game.borderCloseTime = Integer.parseInt(args[2]);
            }
            this.walls.game.borderCloseSpeed = Config.data.getInt("theWalls.autoExecute.speedOfBorderClose");
            if (args.length >= 4) {
                this.walls.game.borderCloseSpeed = Integer.parseInt(args[3]);
            }
            this.walls.game.eventCooldown = Config.data.getInt("theWalls.autoExecute.eventCooldown");
            if (args.length >= 5) {
                this.walls.game.eventCooldown = Integer.parseInt(args[4]);
            }
            if (sender instanceof Player) {
                this.walls.game.start((Player) sender);
            } else {
                this.walls.game.start(null);
            }
        } catch (Exception ex) {
            sender.sendMessage(Utils.formatText("&cAn error occured while starting The Walls!"));
            Utils.getPlugin().getLogger().warning(ex.toString());
        }


        return false;
    }
}
