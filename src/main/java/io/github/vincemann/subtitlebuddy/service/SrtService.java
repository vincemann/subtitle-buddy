package io.github.vincemann.subtitlebuddy.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Singleton
public class SrtService {

    private SrtParserEngine srtParserEngine;


    @Inject
    public SrtService(SrtParserEngine srtParserEngine) {
        this.srtParserEngine = srtParserEngine;
    }

    public void startParser(){
        srtParserEngine.start();
    }

}
