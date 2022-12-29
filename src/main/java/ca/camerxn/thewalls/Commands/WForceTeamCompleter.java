package ca.camerxn.thewalls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WForceTeamCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return Collections.emptyList();
        }
        if (args.length == 2) {
            ArrayList<String> teams = new ArrayList<>();
            teams.add("red");
            teams.add("green");
            teams.add("yellow");
            teams.add("blue");
            return teams;
        }

        return null;
    }
}
