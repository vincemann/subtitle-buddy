package io.github.vincemann.subtitleBuddy.service;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SrtDisplayerEventHandler;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParserEventHandler;
import io.github.vincemann.subtitleBuddy.inputListeners.HotKeyEventHandler;
import io.github.vincemann.subtitleBuddy.inputListeners.MouseClickedEventHandler;
import io.github.vincemann.subtitleBuddy.inputListeners.keyListener.KeyListener;
import io.github.vincemann.subtitleBuddy.inputListeners.mouseListener.MouseListener;

@Singleton
public class EventHandlerService {

    private EventBus eventBus;
    private SrtDisplayerEventHandler srtDisplayerEventHandler;
    private HotKeyEventHandler hotKeyEventHandler;
    private SrtParserEventHandler srtParserEventHandler;
    private MouseClickedEventHandler mouseClickedEventHandler;
    private KeyListener keyListener;
    private MouseListener mouseListener;

    @Inject
    public EventHandlerService(EventBus eventBus, SrtDisplayerEventHandler srtDisplayerEventHandler, HotKeyEventHandler hotKeyEventHandler, SrtParserEventHandler srtParserEventHandler, MouseClickedEventHandler mouseClickedEventHandler, KeyListener keyListener, MouseListener mouseListener) {
        this.eventBus = eventBus;
        this.srtDisplayerEventHandler = srtDisplayerEventHandler;
        this.hotKeyEventHandler = hotKeyEventHandler;
        this.srtParserEventHandler = srtParserEventHandler;
        this.mouseClickedEventHandler = mouseClickedEventHandler;
        this.keyListener = keyListener;
        this.mouseListener = mouseListener;
    }

    public void initEventHandlers(){
        eventBus.register(hotKeyEventHandler);
        eventBus.register(mouseClickedEventHandler);
        eventBus.register(srtDisplayerEventHandler);
        eventBus.register(srtParserEventHandler);
    }
}
