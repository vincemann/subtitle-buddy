package io.github.vincemann.subtitlebuddy.listeners.key;


import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.EndMovieModeHotkeyPressedEvent;
import io.github.vincemann.subtitlebuddy.events.NextClickHotkeyPressedEvent;
import io.github.vincemann.subtitlebuddy.events.SpaceHotkeyPressedEvent;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import lombok.extern.log4j.Log4j2;

/**
 * Listens for global hotkeys and translates to respective events.
 * Events are handled by {@link io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler}.
 */
@Log4j2
@Singleton
public class GlobalHotKeyListener implements NativeKeyListener, KeyListener {

    private EventBus eventBus;
    private boolean alt = false;

    private SrtDisplayerOptions options;


    @Inject
    public GlobalHotKeyListener(EventBus eventBus, SrtDisplayerOptions options) {
        this.eventBus = eventBus;
        this.options = options;
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        //todo let user change hotkeys
        if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
            log.debug("space hotkey pressed");
            eventBus.post(new SpaceHotkeyPressedEvent());
            //alt +n
        } else if (e.getKeyCode() == NativeKeyEvent.VC_N) {
            if (alt) {
                log.debug("next click counts hotkey pressed (alt + n)");
                eventBus.post(new NextClickHotkeyPressedEvent());
            }
            //alt + esc
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            if (options.getBackViaEsc()){
                log.debug("end movie mode hotkey pressed (escape)");
                eventBus.post(new EndMovieModeHotkeyPressedEvent());
            }else{
                log.debug("escape pressed, but ignored via config");
            }
        } else if (e.getKeyCode() == NativeKeyEvent.VC_ALT || e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            log.debug("alt pressed");
            alt = true;
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ALT || e.getKeyCode() == NativeKeyEvent.VC_ALT) {
            log.debug("alt released");
            this.alt = false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

}
