package io.github.vincemann.subtitlebuddy.listeners;

import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.event.SrtDisplayerProvider;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.options.*;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import lombok.extern.log4j.Log4j2;

/**
 * Handles user input events like hot key presses and mouse clicks.
 */
@Singleton
@Log4j2
public class UserInputEventHandler implements HotKeyEventHandler, MouseClickedEventHandler {

    private SrtPlayer srtPlayer;
    private boolean nextClickCounts;
    private SrtDisplayerProvider srtDisplayerProvider;
    private EventBus eventBus;

    private OptionsManager optionsManager;
    private SrtDisplayerOptions options;


    @Inject
    public UserInputEventHandler(SrtPlayer srtPlayer,
                                 SrtDisplayerOptions options,
                                 SrtDisplayerProvider srtDisplayerProvider,
                                 EventBus eventBus,
                                 OptionsManager optionsManager
    ) {
        this.srtPlayer = srtPlayer;
        this.options = options;
        this.optionsManager = optionsManager;
        this.nextClickCounts = false;
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.eventBus = eventBus;
    }


    @Override
    @Subscribe
    public synchronized void handleNextClickHotKeyPressedEvent(NextClickHotkeyPressedEvent event) {
        log.debug("next click hotkey event recognized");
        handleNextClickHotKey();
    }

    @Override
    @Subscribe
    public synchronized void handleSpaceHotKeyPressedEvent(SpaceHotkeyPressedEvent event) {
        log.debug("start stop hotkey event received");
        if (options.getSpaceHotkeyEnabled()) {
            switchParserRunningState();
        } else {
            log.warn("space hotkey pressed but is disabled");
        }
    }

    @Override
    @Subscribe
    public synchronized void handleMovieModeHotKeyPressedEvent(EndMovieModeHotkeyPressedEvent event) {
        log.debug("end movie mode hotkey event arrived");
        // only switch if actually in movie mode
        if (srtDisplayerProvider.getCurrentDisplayer().equals(MovieSrtDisplayer.class))
            eventBus.post(new SwitchSrtDisplayerEvent(SettingsSrtDisplayer.class));
        else
            log.debug("not in movie mode - ignoring end movie mode action");
    }



    private void handleNextClickHotKey() {
        if (options.getNextClickHotkeyEnabled()) {
            if (nextClickCounts) {
                log.trace("next click counts was active, disabling now");
                nextClickCounts = false;
                srtDisplayerProvider.get(MovieSrtDisplayer.class).hideNextClickCounts();
                srtDisplayerProvider.get(SettingsSrtDisplayer.class).hideNextClickCounts();
            } else {
                log.trace("next click counts was not active, enabling now");
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
    public synchronized void handleToggleSpaceHotKeyEvent(ToggleSpaceHotkeyEvent event) {
        //todo toggle tests
        log.debug("toggle space hotkey event arrived:" + event);
        optionsManager.updateSpaceHotkeyEnabled(event.isEnabled());
    }

    @Override
    @Subscribe
    public synchronized void handleToggleNextClickHotKeyEvent(ToggleNextClickHotkeyEvent event) {
        //todo toggle tests
        log.debug("toggle next click hotkey event arrived:" + event);
        optionsManager.updateNextClickHotkeyEnabled(event.isEnabled());
    }

    @Override
    @Subscribe
    public synchronized void handleToggleBackViaEscEvent(ToggleEndMovieModeHotkeyEvent event) {
        //todo toggle tests
        log.debug("toggle end movie mode hotkey event arrived:" + event);
        optionsManager.updateEndMovieModeHotkeyEnabled(event.isEnabled());
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
        synchronized (srtPlayer) {
            log.debug("srt parser state: " + srtPlayer.getCurrentState());
            if (srtPlayer.getCurrentState().equals(RunningState.STATE_RUNNING)) {
                log.trace("stopping srtParser");
                srtPlayer.stop();
            } else if (srtPlayer.getCurrentState().equals(RunningState.STATE_SUSPENDED) || srtPlayer.getCurrentState().equals(RunningState.STATE_UNSTARTED)) {
                log.trace("starting srtParser");
                srtPlayer.start();
            } else {
                log.error("invalid state for switching ParserState : " + srtPlayer.getCurrentState().toString());
            }
        }
    }
}
