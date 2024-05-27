package io.github.vincemann.subtitlebuddy.srt.engine;

import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j2;

/**
 * Starts and manages the background thread in which updates take place.
 * The background thread updates the ui in form of {@link SrtDisplayer} in a given update interval and only when needed.
 */
@Log4j2
@Singleton
public class SrtParserEngineImpl extends SrtParserEngine implements Runnable {

    private Thread updaterThread;
    private boolean stopped = false;
    private SrtPlayer srtPlayer;
    private Provider<SrtDisplayer> srtDisplayerProvider;
    private EventBus eventBus;
    private SubtitleText lastSubtitleText;

    private Timestamp lastTimeStamp;

    @Inject
    public SrtParserEngineImpl(@Named(PropertyFileKeys.UPDATER_DELAY) long updateDelay, SrtPlayer srtPlayer, Provider<SrtDisplayer> srtDisplayerProvider, EventBus eventBus) {
        super(updateDelay);
        this.srtPlayer = srtPlayer;
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.eventBus = eventBus;
        this.updaterThread = new Thread(this);
        this.lastTimeStamp = Timestamp.ZERO();
    }

    @Override
    public void start() {
        if (!updaterThread.isAlive()) {
            log.debug("updater started");
            updaterThread.start();
        } else {
            log.error("UpdaterThread already started!");
            throw new IllegalStateException("UpdaterThread already started!");
        }
    }

    @Override
    public void stop() {
        this.stopped = true;
    }

    @Override
    public void run() {
        while (!stopped) {
            updateProgram();
            try {
                Thread.sleep(getUpdateDelay());
            } catch (InterruptedException e) {
                log.error("updater thread got interrupted ", e);
            }
        }
    }

    private void updateProgram() {
        SrtDisplayer srtDisplayer = this.srtDisplayerProvider.get();
        updateTime(srtDisplayer);

        try {
            log.trace("updater updating current Subtitle");
            srtPlayer.updateCurrentSubtitle();
            SubtitleText currSubtitleText = srtPlayer.getCurrentSubtitleText();

            //log.trace("new subtitle is present: " + currSubtitleText);
            if (lastSubtitleText != null) {
                if (!lastSubtitleText.equals(currSubtitleText)) {
                    // update necessary
                    log.trace("displaying subtitle " + currSubtitleText);
                    srtDisplayer.displaySubtitle(currSubtitleText);
                } else {
                    log.trace("subtitle is identical to last subtitle, no update neccassary");
                }
            } else {
                log.trace("no last subtitle found -> displaying subtitle " + currSubtitleText);
                srtDisplayer.displaySubtitle(currSubtitleText);
            }
            lastSubtitleText = currSubtitleText;
        } catch (TimeStampOutOfBoundsException e) {
            log.info("received TimeStamp out of bounds exception -> doneParsing event triggered");
            eventBus.post(new DoneParsingEvent());
        }
    }

    /**
     * Only update UI when new second reached
     */
    private void updateTime(SrtDisplayer displayer){
        Timestamp time = srtPlayer.getTime();
        if (!lastTimeStamp.equalBySeconds(time)) {
            if (displayer instanceof SettingsSrtDisplayer) {
                ((SettingsSrtDisplayer) displayer).displayTime(time);
                lastTimeStamp = new Timestamp(time);
            }
        }
    }


    @Override
    public void update() {
        log.debug("manual update for srt parser");
        SrtDisplayer srtDisplayer = this.srtDisplayerProvider.get();
        SubtitleText currSubtitleText = srtPlayer.getCurrentSubtitleText();
        srtDisplayer.displaySubtitle(currSubtitleText);
    }
}
