package io.github.vincemann.subtitlebuddy.listeners.mouse;


import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.MouseClickedEvent;
import lombok.extern.log4j.Log4j2;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

/**
 * Listens for global mouse clicks and translates to respective events.
 * Events are handled by {@link io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler}.
 */
@Log4j2
@Singleton
public class GlobalMouseListener implements NativeMouseInputListener, MouseListener {

    private EventBus eventBus;

    @Inject
    public GlobalMouseListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public synchronized void nativeMouseClicked(NativeMouseEvent e) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        switch (e.getButton()) {
            case NativeMouseEvent.BUTTON1:
                eventBus.post(new MouseClickedEvent(MouseKey.LEFT));
                break;
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {
    }
}
