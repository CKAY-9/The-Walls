package ca.camerxn.thewalls.Events;

import ca.camerxn.thewalls.Utils;

public abstract class Event {
    public abstract void run();
    public Event(String eventName) {
        Utils.getPlugin().getLogger().info("Registered event: " + eventName);
        Events.events.add(this);
    }
}
