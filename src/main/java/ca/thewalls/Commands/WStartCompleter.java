package ca.thewalls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import ca.thewalls.TheWalls;

import java.util.Collections;
import java.util.List;

public class WStartCompleter implements TabCompleter {
    public TheWalls walls;
    public WStartCompleter(TheWalls walls) {
        this.walls = walls;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return Collections.singletonList("size");
        }
        if (args.length == 2) {
            return Collections.singletonList("prepTime");
        }
        if (args.length == 3) {
            return Collections.singletonList("timeUntilBorderCloses");
        }
        if (args.length == 4) {
            return Collections.singletonList("borderCloseSpeed");
        }
        if (args.length == 5) {
            return Collections.singletonList("eventCooldown");
        }
        if (args.length >= 6) {
            return Collections.emptyList();
        }

        return null;
    }
}
