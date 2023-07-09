package ca.thewalls.Walls;
import org.bukkit.Location;
import org.bukkit.Material;

public class TempBlock {
    public Location loc;
    public Material block;

    public TempBlock(Location loc, Material mat) {
        this.loc = loc;
        this.block = mat;
    }
}