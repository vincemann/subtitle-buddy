package io.github.vincemann.subtitlebuddy.listeners.mouse;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.MouseClickedEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Listens for global mouse clicks and translates to respective events.
 * Events are handled by {@link io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler}.
 */
@Slf4j
@Singleton
public class GlobalMouseListener implements NativeMouseInputListener, MouseListener {

    private EventBus eventBus;

    @Inject
    public GlobalMouseListener(EventBus eventBus) {
        this.eventBus = eventBus;
        // ugly workaround for bug in jnativehook - keep it like that
        registerListener();
    }

    private void registerListener(){
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GlobalScreen.addNativeMouseListener(this);
        }).start();
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
