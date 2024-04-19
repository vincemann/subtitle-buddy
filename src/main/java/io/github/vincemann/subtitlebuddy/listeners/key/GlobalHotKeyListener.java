package io.github.vincemann.subtitlebuddy.listeners.key;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.HotKeyPressedEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Listens for global hotkeys and translates to respective events.
 * Events are handled by {@link io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler}.
 */
@Slf4j
@Singleton
public class GlobalHotKeyListener implements NativeKeyListener , KeyListener{

    private EventBus eventBus;
    private boolean alt=false;


    @Inject
    public GlobalHotKeyListener(EventBus eventBus) {
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
            GlobalScreen.addNativeKeyListener(this);
        }).start();
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
            }else if (e.getKeyCode() == NativeKeyEvent.ALT_L_MASK || e.getKeyCode() == NativeKeyEvent.ALT_R_MASK){
                alt=true;
            }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.ALT_L_MASK || e.getKeyCode() == NativeKeyEvent.ALT_R_MASK){
            this.alt=false;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

}
