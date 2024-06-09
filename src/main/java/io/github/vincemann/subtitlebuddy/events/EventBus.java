package io.github.vincemann.subtitlebuddy.events;

public interface EventBus {

    void post(Object event);

    void unregister(Object handler);
    void register(Object handler);
}
