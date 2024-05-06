package io.github.vincemann.subtitlebuddy.listeners.key;


import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.HotKeyPressedEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import lombok.extern.log4j.Log4j2;

/**
 * Listens for global hotkeys and translates to respective events.
 * Events are handled by {@link io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler}.
 */
@Log4j2
@Singleton
public class GlobalHotKeyListener implements NativeKeyListener , KeyListener{

    private EventBus eventBus;
    private boolean alt=false;


    @Inject
    public GlobalHotKeyListener(EventBus eventBus) {
        this.eventBus = eventBus;
        GlobalScreen.addNativeKeyListener(this);
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        //todo let user change hotkeys
            //space
            if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
                log.debug("space hotkey pressed");
                eventBus.post(new HotKeyPressedEvent(HotKey.START_STOP));
                //alt +n
            }else if (e.getKeyCode() == NativeKeyEvent.VC_N){
                if(alt) {
                    log.debug("ne click counts hotkey pressed");
                    eventBus.post(new HotKeyPressedEvent(HotKey.NEXT_CLICK));
                }
                //alt + esc
            }else if(e.getKeyCode() == NativeKeyEvent.VC_ESCAPE){
                if(alt) {
                    log.debug("end movie mode hotkey pressed");
                    eventBus.post(new HotKeyPressedEvent(HotKey.END_MOVIE_MODE));
                }
            }else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L || e.getKeyCode() == NativeKeyEvent.VC_ALT_R){
                alt=true;
            }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L || e.getKeyCode() == NativeKeyEvent.VC_ALT_R){
            this.alt=false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

}
