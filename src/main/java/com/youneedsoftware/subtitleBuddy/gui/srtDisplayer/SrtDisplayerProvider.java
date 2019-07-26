package com.youneedsoftware.subtitleBuddy.gui.srtDisplayer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
@Log4j
public class SrtDisplayerProvider implements Provider<SrtDisplayer> {


    @Inject
    private SettingsSrtDisplayer settingsSrtDisplayer;
    @Inject
    private MovieSrtDisplayer movieSrtDisplayer;

    private Class<? extends SrtDisplayer> srtDisplayerMode = SettingsSrtDisplayer.class;



    public synchronized SrtDisplayer get(){
        checkNotNull(movieSrtDisplayer);
        checkNotNull(settingsSrtDisplayer);
        if(srtDisplayerMode.equals(SettingsSrtDisplayer.class)){
            return settingsSrtDisplayer;
        }else if(srtDisplayerMode.equals(MovieSrtDisplayer.class)){
            return movieSrtDisplayer;
        }
        throw new IllegalArgumentException("Unknown srtDisplayerMode: " + srtDisplayerMode.getSimpleName());
    }

    public synchronized <T extends SrtDisplayer> T get(Class<T> srtDisplayerMode){
        if(srtDisplayerMode.equals(SettingsSrtDisplayer.class)){
            return (T)settingsSrtDisplayer;
        }else if(srtDisplayerMode.equals(MovieSrtDisplayer.class)){
            return (T)movieSrtDisplayer;
        }else {
            throw new IllegalArgumentException("Unknown srtDisplayerMode: " + srtDisplayerMode.getSimpleName());
        }
    }

    protected synchronized void setSrtDisplayerMode(Class<? extends SrtDisplayer> srtDisplayerMode) {
        this.srtDisplayerMode = srtDisplayerMode;
    }
}
