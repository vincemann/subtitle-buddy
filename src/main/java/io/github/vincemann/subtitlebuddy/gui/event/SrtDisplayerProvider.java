package io.github.vincemann.subtitlebuddy.gui.event;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import lombok.extern.log4j.Log4j2;

import java.util.List;

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

    public synchronized SrtDisplayer get() {
        checkNotNull(movieSrtDisplayer);
        checkNotNull(settingsSrtDisplayer);
        if (currentDisplayer.equals(SettingsSrtDisplayer.class)) {
            return settingsSrtDisplayer;
        } else if (currentDisplayer.equals(MovieSrtDisplayer.class)) {
            return movieSrtDisplayer;
        }
        throw new IllegalArgumentException("Unknown current srt displayer type: " + currentDisplayer.getSimpleName());
    }

    public Class<? extends SrtDisplayer> getCurrentDisplayer() {
        return currentDisplayer;
    }

    public synchronized <T extends SrtDisplayer> T get(Class<T> displayerClazz) {
        if (displayerClazz.equals(SettingsSrtDisplayer.class)) {
            return (T) settingsSrtDisplayer;
        } else if (displayerClazz.equals(MovieSrtDisplayer.class)) {
            return (T) movieSrtDisplayer;
        } else {
            throw new IllegalArgumentException("Unknown current srt displayer type: " + displayerClazz.getSimpleName());
        }
    }

    public synchronized List<SrtDisplayer> all(){
        return Lists.newArrayList(settingsSrtDisplayer,movieSrtDisplayer);
    }

    protected synchronized void setCurrentDisplayer(Class<? extends SrtDisplayer> currentDisplayer) {
        this.currentDisplayer = currentDisplayer;
    }
}
