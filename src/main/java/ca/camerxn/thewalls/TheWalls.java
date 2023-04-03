package ca.camerxn.thewalls;

import ca.camerxn.thewalls.Commands.*;
import ca.camerxn.thewalls.Listeners.*;
import ca.camerxn.thewalls.Walls.Game;

import org.bukkit.plugin.java.JavaPlugin;

public final class TheWalls extends JavaPlugin {

    @Override
    public void onEnable() {
        // Setup/Check for config file
        Config.initializeData();

        // Register commands
        this.getCommand("wstart").setExecutor(new WStart());
        this.getCommand("wstart").setTabCompleter(new WStartCompleter());
        this.getCommand("wend").setExecutor(new WEnd());
        this.getCommand("wforceteam").setExecutor(new WForceTeam());
        this.getCommand("wforceteam").setTabCompleter(new WForceTeamCompleter());
        this.getCommand("wleaderboard").setExecutor(new WLeaderboard());

        // Register Listeners
        this.getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerAttack(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
    }

    @Override
    public void onDisable() {
        if (Game.started) {
            Game.end(true, null);
        }
    }
}
