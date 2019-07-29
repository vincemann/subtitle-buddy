package io.github.vincemann.subtitleBuddy.srt.updater;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitleBuddy.srt.subtitleFile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SettingsSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SrtDisplayer;
import io.github.vincemann.subtitleBuddy.srt.SubtitleText;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParser;
import lombok.extern.log4j.Log4j;

@Log4j
@Singleton
public class SrtParserUpdaterImpl extends SrtParserUpdater implements Runnable{

    private Thread updaterThread;
    private boolean stopped=false;
    private SrtParser srtParser;
    private Provider<SrtDisplayer> srtDisplayerProvider;
    private EventBus eventBus;
    private SubtitleText lastSubtitleText;

    @Inject
    public SrtParserUpdaterImpl(@Named(PropertyFileKeys.UPDATER_DELAY_KEY) long updateDelay, SrtParser srtParser, Provider<SrtDisplayer> srtDisplayerProvider, EventBus eventBus) {
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
            //log.trace( "updater updating current Subtitle");
            srtParser.updateCurrentSubtitle();
            SubtitleText currSubtitleText = srtParser.getCurrentSubtitleText();

                //log.trace("new subtitle is present: " + currSubtitleText);
                if(lastSubtitleText !=null) {
                    if (!lastSubtitleText.equals(currSubtitleText)) {
                        //update neccessary
                        //log.trace("displaying subtitle ");
                        srtDisplayer.displaySubtitle(currSubtitleText);
                    }else {
                        //log.trace("subtitle is identical to last subtitle, no update neccassary");
                    }
                }else {
                    log.trace("no last subtitle found -> displaying subtitle "+ currSubtitleText);
                    srtDisplayer.displaySubtitle(currSubtitleText);
                }
                lastSubtitleText =currSubtitleText;
        } catch (TimeStampOutOfBoundsException e) {
            log.debug("received TimeStampOutofboundsException -> doneParsing event triggered");
            eventBus.post(new DoneParsingEvent());
            return;
        }
    }


    @Override
    public void update() {
        updateProgram();
    }
}
