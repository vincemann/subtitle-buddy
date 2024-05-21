package io.github.vincemann.subtitlebuddy.events;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.gui.event.SrtDisplayerEventHandler;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsStageController;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.options.OptionsEventHandler;
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

    private OptionsEventHandler optionsEventHandler;
    private KeyListener keyListener;
    private MouseListener mouseListener;

    private OptionsStageController optionsStageController;

    private MovieSrtDisplayer movieSrtDisplayer;

    private SettingsSrtDisplayer settingsSrtDisplayer;

    @Inject
    public EventHandlerRegistrar(EventBus eventBus,
                                 SrtDisplayerEventHandler srtDisplayerEventHandler,
                                 HotKeyEventHandler hotKeyEventHandler,
                                 SrtParserEventHandler srtParserEventHandler,
                                 MouseClickedEventHandler mouseClickedEventHandler,
                                 OptionsEventHandler optionsEventHandler, KeyListener keyListener,
                                 MouseListener mouseListener, OptionsStageController optionsStageController, MovieSrtDisplayer movieSrtDisplayer, SettingsSrtDisplayer settingsSrtDisplayer) {
        this.eventBus = eventBus;
        this.srtDisplayerEventHandler = srtDisplayerEventHandler;
        this.hotKeyEventHandler = hotKeyEventHandler;
        this.srtParserEventHandler = srtParserEventHandler;
        this.mouseClickedEventHandler = mouseClickedEventHandler;
        this.optionsEventHandler = optionsEventHandler;
        this.keyListener = keyListener;
        this.mouseListener = mouseListener;
        this.optionsStageController = optionsStageController;
        this.movieSrtDisplayer = movieSrtDisplayer;
        this.settingsSrtDisplayer = settingsSrtDisplayer;
    }

    public void unregisterEventHandlers(){
        eventBus.unregister(hotKeyEventHandler);
        eventBus.unregister(mouseClickedEventHandler);
        eventBus.unregister(srtDisplayerEventHandler);
        eventBus.unregister(srtParserEventHandler);
        eventBus.unregister(optionsEventHandler);
    }

    public void registerEventHandlers(){
        eventBus.register(hotKeyEventHandler);
        eventBus.register(mouseClickedEventHandler);
        eventBus.register(srtDisplayerEventHandler);
        eventBus.register(srtParserEventHandler);
        eventBus.register(optionsEventHandler);
        eventBus.register(optionsStageController);
    }
}
