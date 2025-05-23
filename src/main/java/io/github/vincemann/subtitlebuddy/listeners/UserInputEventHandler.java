package io.github.vincemann.subtitlebuddy.listeners;

import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.event.SrtDisplayerProvider;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.options.*;
import io.github.vincemann.subtitlebuddy.srt.SrtOptions;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
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
    private SrtDisplayerOptions srtDisplayerOptions;

    private SrtOptions srtOptions;


    @Inject
    public UserInputEventHandler(SrtPlayer srtPlayer,
                                 SrtDisplayerOptions srtDisplayerOptions,
                                 SrtDisplayerProvider srtDisplayerProvider,
                                 EventBus eventBus,
                                 OptionsManager optionsManager,
                                 SrtOptions srtOptions
    ) {
        this.srtPlayer = srtPlayer;
        this.srtDisplayerOptions = srtDisplayerOptions;
        this.optionsManager = optionsManager;
        this.srtOptions = srtOptions;
        this.nextClickCounts = false;
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.eventBus = eventBus;
    }

    @Override
    @Subscribe
    public synchronized void handleChangeDefaultSubtitleVisibilityEvent(DefaultSubtitleVisibilityHotKeyPressedEvent event) throws TimeStampOutOfBoundsException {
        //todo toggle tests
        log.debug("DefaultSubtitleVisibilityHotKeyPressedEvent event received");
        boolean visible = srtOptions.isDefaultSubtitleVisible();
        srtOptions.updateDefaultSubtitleVisible(!visible);
        srtPlayer.updateCurrentSubtitle();
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    @Override
    @Subscribe
    public synchronized void handleNextClickHotKeyPressedEvent(NextClickHotkeyPressedEvent event) {
        log.debug("next click hotkey event received");
        handleNextClickHotKey();
    }

    @Override
    @Subscribe
    public synchronized void handleSpaceHotKeyPressedEvent(SpaceHotkeyPressedEvent event) {
        log.debug("start stop hotkey event received");
        if (srtDisplayerOptions.getSpaceHotkeyEnabled()) {
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
        if (srtDisplayerOptions.getNextClickHotkeyEnabled()) {
            if (nextClickCounts) {
                log.trace("next click counts was active, disabling now");
                nextClickCounts = false;
                srtDisplayerProvider.all().forEach(SrtDisplayer::hideNextClickCounts);
            } else {
                log.trace("next click counts was not active, enabling now");
                srtDisplayerProvider.all().forEach(SrtDisplayer::displayNextClickCounts);
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
        srtDisplayerOptions.updateSpaceHotkeyEnabled(event.isEnabled());
    }

    @Override
    @Subscribe
    public synchronized void handleToggleNextClickHotKeyEvent(ToggleNextClickHotkeyEvent event) {
        //todo toggle tests
        log.debug("toggle next click hotkey event arrived:" + event);
        srtDisplayerOptions.updateNextClickHotkeyEnabled(event.isEnabled());
    }

    @Override
    @Subscribe
    public synchronized void handleToggleBackViaEscEvent(ToggleEndMovieModeHotkeyEvent event) {
        //todo toggle tests
        log.debug("toggle end movie mode hotkey event arrived:" + event);
        srtDisplayerOptions.updateEndMovieModeHotkeyEnabled(event.isEnabled());
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
                    srtDisplayerProvider.all().forEach(SrtDisplayer::hideNextClickCounts);
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
