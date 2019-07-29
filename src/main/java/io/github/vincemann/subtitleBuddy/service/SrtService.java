package io.github.vincemann.subtitleBuddy.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitleBuddy.srt.updater.SrtParserUpdater;
import lombok.extern.log4j.Log4j;


@Log4j
@Singleton
public class SrtService {

    private SrtParserUpdater srtParserUpdater;


    @Inject
    public SrtService(SrtParserUpdater srtParserUpdater) {
        this.srtParserUpdater = srtParserUpdater;
    }

    public void startParser(){
        srtParserUpdater.start();
    }

}
