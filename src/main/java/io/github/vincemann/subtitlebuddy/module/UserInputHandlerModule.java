package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.key.KeyListener;
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.GlobalMouseListener;
import lombok.extern.log4j.Log4j2;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;


@Log4j2
public class UserInputHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        registerHook();
        bind(HotKeyEventHandler.class).to(UserInputEventHandler.class);
        bind(MouseClickedEventHandler.class).to(UserInputEventHandler.class);
        bind(KeyListener.class).to(GlobalHotKeyListener.class);
        bind(MouseListener.class).to(GlobalMouseListener.class);
    }


    // this works for 2.0.2 patched
    private void registerHook(){
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            log.error("could not register Native Hook, caused by: ",ex);
        }
    }
}
