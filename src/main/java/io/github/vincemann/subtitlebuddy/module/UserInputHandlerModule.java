package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.UserInputEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.key.KeyListener;
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.GlobalMouseListener;
import lombok.extern.slf4j.Slf4j;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;


@Slf4j
public class UserInputHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
        registerHook();
        bind(HotKeyEventHandler.class).to(UserInputEventHandler.class);
        bind(MouseClickedEventHandler.class).to(UserInputEventHandler.class);
        bind(KeyListener.class).to(GlobalHotKeyListener.class);
        bind(MouseListener.class).to(GlobalMouseListener.class);
    }


    private void registerHook(){
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            log.error("could not register Native Hook, caused by: ",ex);
        }
    }



}
