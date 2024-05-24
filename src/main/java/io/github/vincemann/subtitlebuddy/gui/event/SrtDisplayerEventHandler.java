package io.github.vincemann.subtitlebuddy.gui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.RequestSubtitleUpdateEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Singleton
public class SrtDisplayerEventHandler {

    private SrtDisplayerProvider srtDisplayerProvider;
    private WindowManager windowManager;
    private EventBus eventBus;

    @Inject
    public SrtDisplayerEventHandler(SrtDisplayerProvider srtDisplayerProvider, WindowManager windowManager, EventBus eventBus) {
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.windowManager = windowManager;
        this.eventBus = eventBus;
    }

    @Subscribe
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent event){
        log.debug("switching srt displayer event arrived");
        String target = windowOf(event.getTarget());
        if (target.equals(Windows.SETTINGS)){
            Platform.runLater(() -> {
                windowManager.close(Windows.MOVIE);
                windowManager.open(Windows.SETTINGS);
            });
        }
        else{
            Platform.runLater(() -> {
                windowManager.close(Windows.SETTINGS);
                windowManager.open(Windows.MOVIE);
            });
        }
        srtDisplayerProvider.setCurrentDisplayer(event.getTarget());
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    private String windowOf(Class<? extends SrtDisplayer> displayer){
        if (displayer.equals(SettingsSrtDisplayer.class))
            return Windows.SETTINGS;
        else
            return Windows.MOVIE;
    }
}
