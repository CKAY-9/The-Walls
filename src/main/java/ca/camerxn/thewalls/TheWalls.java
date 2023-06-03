package ca.camerxn.thewalls;

import ca.camerxn.thewalls.Commands.*;
import ca.camerxn.thewalls.Listeners.*;
import ca.camerxn.thewalls.Walls.Game;
import ca.camerxn.thewalls.Walls.World;

import org.bukkit.plugin.java.JavaPlugin;

public final class TheWalls extends JavaPlugin {

    public Game game = new Game(this);
    public World world = new World(this);
    public Utils utils = new Utils(this);

    @Override
    public void onEnable() {
        // Setup/Check for config file
        Config.initializeData();

        // Register commands
        this.getCommand("wstart").setExecutor(new WStart(this));
        this.getCommand("wstart").setTabCompleter(new WStartCompleter(this));
        this.getCommand("wend").setExecutor(new WEnd(this));
        this.getCommand("wforceteam").setExecutor(new WForceTeam(this));
        this.getCommand("wforceteam").setTabCompleter(new WForceTeamCompleter(this));
        this.getCommand("wleaderboard").setExecutor(new WLeaderboard(this));

        // Register Listeners
        this.getServer().getPluginManager().registerEvents(new PlayerLeave(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerAttack(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityDeath(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
    }

    @Override
    public void onDisable() {
        if (game.started) {
            game.end(true, null);
        }
    }
}
