package ca.thewalls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.thewalls.Config;
import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

public class WEvents implements CommandExecutor {
    public TheWalls walls;
    public WEvents(TheWalls walls) {
        this.walls = walls;
    }

    // This is pub static because Walls/Game.java also uses this to notify players
    public static void annouceEvents(Player sender) {
        sender.sendMessage(Utils.formatText("&a[The Walls] Enabled Events: "));

        if (Config.data.getBoolean("events.tnt.enabled"))
            sender.sendMessage(Utils.formatText("&d  - TNT Spawn"));
        if (Config.data.getBoolean("events.blindSnail.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Blind Snail"));
        if (Config.data.getBoolean("events.locationSwap.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Location Swap"));
        if (Config.data.getBoolean("events.supplyChest.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Supply Chest"));
        if (Config.data.getBoolean("events.gregs.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Gregs / Free Food"));
        if (Config.data.getBoolean("events.reveal.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Location Reveal"));
        if (Config.data.getBoolean("events.sinkHole.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Sink Hole"));
        if (Config.data.getBoolean("events.hailStorm.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Hail of Arrows"));
        if (Config.data.getBoolean("events.bossMan.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Boss Man"));
        if (Config.data.getBoolean("events.itemCheck.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Item Check"));
        if (Config.data.getBoolean("events.bombingRun.enabled"))
            sender.sendMessage(Utils.formatText("&d  - Bombing Run"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            annouceEvents((Player)sender);
        }

        return false;
    }

}
