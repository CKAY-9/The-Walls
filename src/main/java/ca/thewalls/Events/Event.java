package ca.thewalls.Events;

import ca.thewalls.TheWalls;
import ca.thewalls.Utils;

public abstract class Event {
    public abstract void run();

    public TheWalls walls;

    public Event(String eventName, TheWalls walls) {
        Utils.getPlugin().getLogger().info("Registered event: " + eventName);
        Events.events.add(this);

        this.walls = walls;
    }
}
