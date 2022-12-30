package ca.camerxn.thewalls.Commands;

import ca.camerxn.thewalls.Config;
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
            teams.add(Config.data.getString("teams.zero.name"));
            teams.add(Config.data.getString("teams.one.name"));
            teams.add(Config.data.getString("teams.two.name"));
            teams.add(Config.data.getString("teams.three.name"));
            return teams;
        }

        return null;
    }
}
