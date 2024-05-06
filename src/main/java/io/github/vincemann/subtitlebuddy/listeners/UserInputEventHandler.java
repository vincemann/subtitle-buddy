package io.github.vincemann.subtitlebuddy.listeners;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.events.HotKeyPressedEvent;
import io.github.vincemann.subtitlebuddy.events.MouseClickedEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.properties.PropertyAccessException;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayerProvider;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKeyEventHandler;
import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseClickedEventHandler;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParser;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import lombok.extern.log4j.Log4j2;

/**
 * Handles user input events like hot key presses and mouse clicks.
 */
@Singleton
@Log4j2
public class UserInputEventHandler implements HotKeyEventHandler, MouseClickedEventHandler {

    private PropertiesFile properties;
    private final SrtParser srtParser;
    private boolean nextClickCounts;
    private boolean startStopHotKeyToggled;
    private boolean nextClickHotKeyToggled;
    private SrtDisplayerProvider srtDisplayerProvider;
    private EventBus eventBus;


    @Inject
    public UserInputEventHandler(SrtParser srtParser, PropertiesFile properties,
                                 @Named(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED) boolean startStopHotKeyToggled,
                                 @Named(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED) boolean nextClickHotKeyToggled ,
                                 SrtDisplayerProvider srtDisplayerProvider,
                                 EventBus eventBus) {
        this.properties = properties;
        this.srtParser = srtParser;
        this.nextClickCounts=false;
        this.startStopHotKeyToggled=startStopHotKeyToggled;
        this.nextClickHotKeyToggled=nextClickHotKeyToggled;
        this.srtDisplayerProvider=srtDisplayerProvider;
        this.eventBus=eventBus;
    }


    /**
     * Handles hot key events.
     * @param e
     */
    @Override
    @Subscribe
    public synchronized void handleHotKeyPressedEvent(HotKeyPressedEvent e) {
        log.trace("hotKey event getting handled");
        switch (e.getHotKey()){
            case NEXT_CLICK:
                log.trace("next click hotkey event recognized");
                handleNextClickHotKey();
                break;
            case START_STOP:
                log.trace("start stop hotkey event received");
                if(!startStopHotKeyToggled) {
                    switchParserRunningState();
                }else {
                    log.warn("space pressed but is toggled");
                }
                break;
            case END_MOVIE_MODE:
                log.trace("end movie mode hotkey event arrived");
                MovieSrtDisplayer movieSrtDisplayer = srtDisplayerProvider.get(MovieSrtDisplayer.class);
                if(movieSrtDisplayer.isDisplaying()){
                    log.debug("switching from movie mode to settings mode bc of end movie mode hotkey event");
                    eventBus.post(new SwitchSrtDisplayerEvent(SettingsSrtDisplayer.class));
                }
        }
    }

    private void handleNextClickHotKey(){
        if(!nextClickHotKeyToggled) {
            if(nextClickCounts){
                log.trace("next click counts was active, disabling now");
                nextClickCounts=false;
                srtDisplayerProvider.get(MovieSrtDisplayer.class).hideNextClickCounts();
                srtDisplayerProvider.get(SettingsSrtDisplayer.class).hideNextClickCounts();
            }else {
                log.trace("next click counts wasnt active, enabling now");
                srtDisplayerProvider.get(MovieSrtDisplayer.class).displayNextClickCounts();
                srtDisplayerProvider.get(SettingsSrtDisplayer.class).displayNextClickCounts();
                nextClickCounts=true;
            }
        }else {
            log.warn("f7 pressed but is toggled");
        }
    }

    @Override
    @Subscribe
    public synchronized void handleToggleHotKeyEvent(ToggleHotKeyEvent e) {
        //todo toggle tests
        log.debug("hot key toggled: " + e);
        switch (e.getHotKey()) {
            case START_STOP:
                this.startStopHotKeyToggled = e.isToggled();
                try {
                    properties.saveProperty(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED,e.isToggled());
                } catch (PropertyAccessException e1) {
                    log.debug("coult not save property",e1);
                }
                break;
            case NEXT_CLICK:
                this.nextClickHotKeyToggled = e.isToggled();
                try {
                    properties.saveProperty(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED,e.isToggled());
                } catch (PropertyAccessException e1) {
                    log.debug("coult not save property",e1);
                }

                break;
        }

    }

    @Subscribe
    @Override
    public synchronized void handleMouseClickedEvent(MouseClickedEvent e) {
        log.trace("mouse clicked event getting handled");
        switch (e.getMouseKey()){
            case LEFT:
                if(nextClickCounts){
                    switchParserRunningState();
                    nextClickCounts=false;
                    srtDisplayerProvider.get(MovieSrtDisplayer.class).hideNextClickCounts();
                    srtDisplayerProvider.get(SettingsSrtDisplayer.class).hideNextClickCounts();
                }
                break;
        }
    }

    private void switchParserRunningState(){
        log.debug("switching parsers running state");
        synchronized (srtParser){
            if(srtParser.getCurrentState().equals(RunningState.STATE_RUNNING)){
                log.trace("stopping io.github.vincemann.srtParser");
                srtParser.stop();
            }else if(srtParser.getCurrentState().equals(RunningState.STATE_SUSPENDED) || srtParser.getCurrentState().equals(RunningState.STATE_UNSTARTED)){
                log.trace("starting io.github.vincemann.srtParser");
                srtParser.start();
            }else {
                log.error("invalid state for switching ParserState : " + srtParser.getCurrentState().toString());
            }
        }
    }
}
