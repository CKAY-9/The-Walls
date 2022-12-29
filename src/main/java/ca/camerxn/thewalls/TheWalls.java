package ca.camerxn.thewalls;

import ca.camerxn.thewalls.Commands.*;
import ca.camerxn.thewalls.Listeners.PlayerAttack;
import ca.camerxn.thewalls.Listeners.PlayerDeath;
import ca.camerxn.thewalls.Listeners.PlayerJoin;
import ca.camerxn.thewalls.Listeners.PlayerLeave;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheWalls extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register commands
        this.getCommand("wstart").setExecutor(new WStart());
        this.getCommand("wstart").setTabCompleter(new WStartCompleter());
        this.getCommand("wend").setExecutor(new WEnd());
        this.getCommand("wforceteam").setExecutor(new WForceTeam());
        this.getCommand("wforceteam").setTabCompleter(new WForceTeamCompleter());

        // Register Listeners
        this.getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerAttack(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
