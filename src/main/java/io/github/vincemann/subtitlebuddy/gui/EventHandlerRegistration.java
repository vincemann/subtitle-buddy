package io.github.vincemann.subtitlebuddy.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class EventHandlerRegistration<T extends Event> {
    private Node node;
    private EventHandler<T> eventHandler;
    private EventType<T> eventType;

    public EventHandlerRegistration(Node node, EventHandler<T> eventHandler, EventType<T> eventType) {
        this.node = node;
        this.eventHandler = eventHandler;
        this.eventType = eventType;
    }

    public void unregister(){
        this.node.removeEventHandler(eventType,eventHandler);
    }
}
