package io.github.vincemann.subtitlebuddy.srt.engine;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParser;
import lombok.extern.log4j.Log4j;

/**
 * Starts and manages the background thread in which updates take place.
 */
@Log4j
@Singleton
public class SrtParserEngineImpl extends SrtParserEngine implements Runnable{

    private Thread updaterThread;
    private boolean stopped=false;
    private SrtParser srtParser;
    private Provider<SrtDisplayer> srtDisplayerProvider;
    private EventBus eventBus;
    private SubtitleText lastSubtitleText;

    @Inject
    public SrtParserEngineImpl(@Named(PropertyFileKeys.UPDATER_DELAY) long updateDelay, SrtParser srtParser, Provider<SrtDisplayer> srtDisplayerProvider, EventBus eventBus) {
        super(updateDelay);
        this.srtParser=srtParser;
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.eventBus=eventBus;
        this.updaterThread=new Thread(this);
    }

    @Override
    public void start() {
        if(!updaterThread.isAlive()) {
            log.debug("updater started");
            updaterThread.start();
        }else {
            log.error("UpdaterThread already startet!");
            throw new IllegalStateException("UpdaterThread already startet!");
        }
    }

    @Override
    public void stop() {
        this.stopped=true;
    }

    @Override
    public void run() {
        while (!stopped){
            updateProgram();
            try {
                Thread.sleep(getUpdateDelay());
            } catch (InterruptedException e) {
                log.error("updater thread got interrupted ", e);
            }
        }
    }

    private synchronized void updateProgram(){
        SrtDisplayer srtDisplayer = this.srtDisplayerProvider.get();
        if(srtDisplayer instanceof SettingsSrtDisplayer){
            ((SettingsSrtDisplayer)srtDisplayer).setTime(srtParser.getTime());
        }

        try {
            log.trace( "updater updating current Subtitle");
            srtParser.updateCurrentSubtitle();
            SubtitleText currSubtitleText = srtParser.getCurrentSubtitleText();

                //log.trace("new subtitle is present: " + currSubtitleText);
                if(lastSubtitleText !=null) {
                    if (!lastSubtitleText.equals(currSubtitleText)) {
                        // update necessary
                        log.trace("displaying subtitle "+currSubtitleText);
                        srtDisplayer.displaySubtitle(currSubtitleText);
                    }else {
                        log.trace("subtitle is identical to last subtitle, no update neccassary");
                    }
                }else {
                    log.trace("no last subtitle found -> displaying subtitle "+ currSubtitleText);
                    srtDisplayer.displaySubtitle(currSubtitleText);
                }
                lastSubtitleText =currSubtitleText;
        } catch (TimeStampOutOfBoundsException e) {
            log.debug("received TimeStampOutofboundsException -> doneParsing event triggered");
            eventBus.post(new DoneParsingEvent());
        }
    }


    @Override
    public void update() {
        updateProgram();
    }
}
