package io.github.vincemann.subtitlebuddy.listeners.mouse;


import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.MouseClickedEvent;
import lombok.extern.log4j.Log4j2;

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
        GlobalScreen.addNativeMouseListener(this);
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
