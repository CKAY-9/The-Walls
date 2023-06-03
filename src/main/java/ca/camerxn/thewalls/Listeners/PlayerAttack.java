package ca.camerxn.thewalls.Listeners;

import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Walls.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerAttack implements Listener {
    public TheWalls walls;

    public PlayerAttack(TheWalls walls) {
        this.walls = walls;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;
        if (!this.walls.game.started) return;

        Player attacker = (Player) e.getDamager();
        Player receiver = (Player) e.getEntity();

        if (Team.getPlayerTeam(attacker, this.walls.game.teams) == Team.getPlayerTeam(receiver, this.walls.game.teams)) {
            e.setDamage(0); // Cancel damage, but still allow for knock-back
        }
    }
}
