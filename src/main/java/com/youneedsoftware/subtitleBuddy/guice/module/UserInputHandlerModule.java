package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.AbstractModule;
import com.youneedsoftware.subtitleBuddy.inputListeners.HotKeyEventHandler;
import com.youneedsoftware.subtitleBuddy.inputListeners.MouseClickedEventHandler;
import com.youneedsoftware.subtitleBuddy.inputListeners.UserInputEventHandler;
import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.KeyListener;
import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.GlobalHotKeyListener;
import com.youneedsoftware.subtitleBuddy.inputListeners.mouseListener.MouseListener;
import com.youneedsoftware.subtitleBuddy.inputListeners.mouseListener.GlobalMouseListener;
import lombok.extern.log4j.Log4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;


@Log4j
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
