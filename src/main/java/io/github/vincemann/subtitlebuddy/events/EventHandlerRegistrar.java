package io.github.vincemann.subtitlebuddy.events;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayerEventHandler;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParserEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.key.KeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseListener;

@Singleton
public class EventHandlerRegistrar {

    private EventBus eventBus;
    private SrtDisplayerEventHandler srtDisplayerEventHandler;
    private HotKeyEventHandler hotKeyEventHandler;
    private SrtParserEventHandler srtParserEventHandler;
    private MouseClickedEventHandler mouseClickedEventHandler;
    private KeyListener keyListener;
    private MouseListener mouseListener;

    @Inject
    public EventHandlerRegistrar(EventBus eventBus,
                                 SrtDisplayerEventHandler srtDisplayerEventHandler,
                                 HotKeyEventHandler hotKeyEventHandler,
                                 SrtParserEventHandler srtParserEventHandler,
                                 MouseClickedEventHandler mouseClickedEventHandler,
                                 KeyListener keyListener,
                                 MouseListener mouseListener) {
        this.eventBus = eventBus;
        this.srtDisplayerEventHandler = srtDisplayerEventHandler;
        this.hotKeyEventHandler = hotKeyEventHandler;
        this.srtParserEventHandler = srtParserEventHandler;
        this.mouseClickedEventHandler = mouseClickedEventHandler;
        this.keyListener = keyListener;
        this.mouseListener = mouseListener;
    }

    public void registerEventHandlers(){
        eventBus.register(hotKeyEventHandler);
        eventBus.register(mouseClickedEventHandler);
        eventBus.register(srtDisplayerEventHandler);
        eventBus.register(srtParserEventHandler);
    }
}