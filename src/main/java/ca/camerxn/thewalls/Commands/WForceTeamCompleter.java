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
            String teamOne = Config.data.getString("teams.zero.name");
            String teamTwo = Config.data.getString("teams.one.name");
            String teamThree = Config.data.getString("teams.two.name");
            String teamFour = Config.data.getString("teams.three.name");

            if (teamOne == null || teamTwo == null || teamThree == null || teamFour == null) return Collections.emptyList();

            ArrayList<String> teams = new ArrayList<>();
            teams.add(teamOne);
            teams.add(teamTwo);
            teams.add(teamThree);
            teams.add(teamFour);
            return teams;
        }

        return null;
    }
}
