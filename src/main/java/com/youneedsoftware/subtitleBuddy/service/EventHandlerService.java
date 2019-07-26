package com.youneedsoftware.subtitleBuddy.service;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.SrtDisplayerEventHandler;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParserEventHandler;
import com.youneedsoftware.subtitleBuddy.inputListeners.HotKeyEventHandler;
import com.youneedsoftware.subtitleBuddy.inputListeners.MouseClickedEventHandler;
import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.KeyListener;
import com.youneedsoftware.subtitleBuddy.inputListeners.mouseListener.MouseListener;

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
