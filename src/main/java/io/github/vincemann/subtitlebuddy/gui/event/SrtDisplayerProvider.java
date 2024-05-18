package io.github.vincemann.subtitlebuddy.gui.event;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import lombok.extern.log4j.Log4j2;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides the currently displayed SrtDisplayer in a dynamic way.
 * Can be either the SettingsSrtDisplayer or the MovieSrtDisplayer.
 * Holds the class object of currently displayed SrtDisplayer as internal state.
 */
@Singleton
@Log4j2
public class SrtDisplayerProvider implements Provider<SrtDisplayer> {


    @Inject
    private SettingsSrtDisplayer settingsSrtDisplayer;
    @Inject
    private MovieSrtDisplayer movieSrtDisplayer;

    private Class<? extends SrtDisplayer> currentDisplayer = SettingsSrtDisplayer.class;

    public synchronized SrtDisplayer get(){
        checkNotNull(movieSrtDisplayer);
        checkNotNull(settingsSrtDisplayer);
        if(currentDisplayer.equals(SettingsSrtDisplayer.class)){
            return settingsSrtDisplayer;
        }else if(currentDisplayer.equals(MovieSrtDisplayer.class)){
            return movieSrtDisplayer;
        }
        throw new IllegalArgumentException("Unknown current srt displayer type: " + currentDisplayer.getSimpleName());
    }

    public synchronized <T extends SrtDisplayer> T get(Class<T> srtDisplayerMode){
        if(srtDisplayerMode.equals(SettingsSrtDisplayer.class)){
            return (T)settingsSrtDisplayer;
        }else if(srtDisplayerMode.equals(MovieSrtDisplayer.class)){
            return (T)movieSrtDisplayer;
        }else {
            throw new IllegalArgumentException("Unknown current srt displayer type: " + srtDisplayerMode.getSimpleName());
        }
    }

    protected synchronized void setCurrentDisplayer(Class<? extends SrtDisplayer> currentDisplayer) {
        this.currentDisplayer = currentDisplayer;
    }
}
