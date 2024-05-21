package io.github.vincemann.subtitlebuddy.gui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.options.OptionsManagerImpl;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Singleton
public class SrtDisplayerEventHandler {

    private SrtDisplayerProvider srtDisplayerProvider;

    private OptionsManagerImpl optionsManager;

    private WindowManager windowManager;

    private FontManager fontManager;

    private EventBus eventBus;

    @Inject
    public SrtDisplayerEventHandler(SrtDisplayerProvider srtDisplayerProvider, OptionsManagerImpl optionsManager, WindowManager windowManager, FontManager fontManager, EventBus eventBus) {
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.optionsManager = optionsManager;
        this.windowManager = windowManager;
        this.fontManager = fontManager;
        this.eventBus = eventBus;
    }

    @Subscribe
    public void handleUpdateFontColorEvent(UpdateFontColorEvent event) {
        log.info("UpdateFontColorEvent arrived ");
        log.debug("changing color to: " + event.getColor().toString());
        optionsManager.updateFontColor(event.getColor());
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    @Subscribe
    public void handleUpdateCurrentFontEvent(UpdateCurrentFontEvent event) {
        log.info("UpdateCurrentFontEvent arrived ");
        optionsManager.updateCurrentFont(event.getFontPath());
        fontManager.reloadCurrentFont();
        eventBus.post(new RequestSubtitleUpdateEvent());
    }


    @Subscribe
    public void handleUpdateSubtitlePosEvent(UpdateSubtitlePosEvent event) {
        optionsManager.updateSubtitlePos(event.getNewPos());
    }

    @Subscribe
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent event){
        log.debug("switching srt displayer event arrived");
        Platform.runLater( () -> windowManager.showWindow(mapToWindow(event.getTarget())));
        srtDisplayerProvider.setCurrentDisplayer(event.getTarget());
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    private String mapToWindow(Class<? extends SrtDisplayer> displayer){
        if (displayer.equals(SettingsSrtDisplayer.class))
            return Windows.SETTINGS;
        else
            return Windows.MOVIE;
    }
}
