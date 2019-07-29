package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.inputListeners.HotKeyEventHandler;
import io.github.vincemann.subtitleBuddy.inputListeners.MouseClickedEventHandler;
import io.github.vincemann.subtitleBuddy.inputListeners.UserInputEventHandler;
import io.github.vincemann.subtitleBuddy.inputListeners.keyListener.KeyListener;
import io.github.vincemann.subtitleBuddy.inputListeners.keyListener.GlobalHotKeyListener;
import io.github.vincemann.subtitleBuddy.inputListeners.mouseListener.MouseListener;
import io.github.vincemann.subtitleBuddy.inputListeners.mouseListener.GlobalMouseListener;
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
