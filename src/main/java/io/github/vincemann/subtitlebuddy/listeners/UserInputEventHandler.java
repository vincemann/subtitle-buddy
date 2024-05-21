package io.github.vincemann.subtitlebuddy.listeners;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.HotKeyPressedEvent;
import io.github.vincemann.subtitlebuddy.events.MouseClickedEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.event.SrtDisplayerProvider;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.options.*;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParser;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import lombok.extern.log4j.Log4j2;

/**
 * Handles user input events like hot key presses and mouse clicks.
 */
@Singleton
@Log4j2
public class UserInputEventHandler implements HotKeyEventHandler, MouseClickedEventHandler {

    private SrtParser srtParser;
    private boolean nextClickCounts;
    private SrtDisplayerProvider srtDisplayerProvider;
    private EventBus eventBus;

    private OptionsManager optionsManager;
    private SrtDisplayerOptions options;


    @Inject
    public UserInputEventHandler(SrtParser srtParser,
                                 SrtDisplayerOptions options,
                                 SrtDisplayerProvider srtDisplayerProvider,
                                 EventBus eventBus,
                                 OptionsManager optionsManager
    ) {
        this.srtParser = srtParser;
        this.options = options;
        this.optionsManager = optionsManager;
        this.nextClickCounts = false;
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.eventBus = eventBus;
    }


    @Override
    @Subscribe
    public synchronized void handleHotKeyPressedEvent(HotKeyPressedEvent e) {
        log.debug("hotKey event getting handled");
        switch (e.getHotKey()) {
            case NEXT_CLICK:
                log.debug("next click hotkey event recognized");
                handleNextClickHotKey();
                break;
            case START_STOP:
                log.debug("start stop hotkey event received");
                if (options.getSpaceHotkeyEnabled()) {
                    switchParserRunningState();
                } else {
                    log.warn("space hotkey pressed but is disabled");
                }
                break;
            case END_MOVIE_MODE:
                log.debug("end movie mode hotkey event arrived");
                eventBus.post(new SwitchSrtDisplayerEvent(SettingsSrtDisplayer.class));
        }
    }

    private void handleNextClickHotKey() {
        if (options.getNextClickHotkeyEnabled()) {
            if (nextClickCounts) {
                log.trace("next click counts was active, disabling now");
                nextClickCounts = false;
                srtDisplayerProvider.get(MovieSrtDisplayer.class).hideNextClickCounts();
                srtDisplayerProvider.get(SettingsSrtDisplayer.class).hideNextClickCounts();
            } else {
                log.trace("next click counts wasnt active, enabling now");
                srtDisplayerProvider.get(MovieSrtDisplayer.class).displayNextClickCounts();
                srtDisplayerProvider.get(SettingsSrtDisplayer.class).displayNextClickCounts();
                nextClickCounts = true;
            }
        } else {
            log.warn("next click hotkey pressed but is disabled");
        }
    }

    @Override
    @Subscribe
    public synchronized void handleToggleHotKeyEvent(ToggleHotKeyEvent event) {
        //todo toggle tests
        log.debug("hot key toggled: " + event);
        switch (event.getHotKey()) {
            case START_STOP:
                optionsManager.updateSpaceHotkeyEnabled(event.isEnabled());
                break;
            case NEXT_CLICK:
                optionsManager.updateNextClickHotkeyEnabled(event.isEnabled());
                break;
        }

    }

    @Subscribe
    @Override
    public synchronized void handleMouseClickedEvent(MouseClickedEvent e) {
        log.trace("mouse clicked event getting handled");
        switch (e.getMouseKey()) {
            case LEFT:
                if (nextClickCounts) {
                    switchParserRunningState();
                    nextClickCounts = false;
                    srtDisplayerProvider.get(MovieSrtDisplayer.class).hideNextClickCounts();
                    srtDisplayerProvider.get(SettingsSrtDisplayer.class).hideNextClickCounts();
                }
                break;
        }
    }

    private void switchParserRunningState() {
        log.debug("switching parsers running state");
        synchronized (srtParser) {
            log.debug("srt parser state: " + srtParser.getCurrentState());
            if (srtParser.getCurrentState().equals(RunningState.STATE_RUNNING)) {
                log.trace("stopping srtParser");
                srtParser.stop();
            } else if (srtParser.getCurrentState().equals(RunningState.STATE_SUSPENDED) || srtParser.getCurrentState().equals(RunningState.STATE_UNSTARTED)) {
                log.trace("starting srtParser");
                srtParser.start();
            } else {
                log.error("invalid state for switching ParserState : " + srtParser.getCurrentState().toString());
            }
        }
    }
}
