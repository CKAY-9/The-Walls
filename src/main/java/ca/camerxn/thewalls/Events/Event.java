package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.TheWalls;
import ca.camerxn.thewalls.Utils;

public abstract class Event {
    public abstract void run();

    public TheWalls walls;

    public Event(String eventName, TheWalls walls) {
        Utils.getPlugin().getLogger().info("Registered event: " + eventName);
        Events.events.add(this);

        this.walls = walls;
    }
}
