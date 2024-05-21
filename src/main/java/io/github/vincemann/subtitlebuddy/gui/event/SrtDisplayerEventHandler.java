package io.github.vincemann.subtitlebuddy.gui.event;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateCurrentFontEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateSubtitlePosEvent;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.options.OptionsManagerImpl;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Singleton
public class SrtDisplayerEventHandler {

    private SrtDisplayerProvider srtDisplayerProvider;

    private OptionsManagerImpl optionsManager;

    private WindowManager windowManager;

    private FontManager fontManager;

    @Inject
    public SrtDisplayerEventHandler(SrtDisplayerProvider srtDisplayerProvider, OptionsManagerImpl optionsManager, WindowManager windowManager, FontManager fontManager) {
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.optionsManager = optionsManager;
        this.windowManager = windowManager;
        this.fontManager = fontManager;
    }

    @Subscribe
    public void handleUpdateFontColorEvent(UpdateFontColorEvent event) {
        log.info("UpdateFontColorEvent arrived ");
        log.debug("changing color to: " + event.getColor().toString());
        optionsManager.updateFontColor(event.getColor());
        srtDisplayerProvider.get().refreshSubtitle();
    }

    @Subscribe
    public void handleUpdateCurrentFontEvent(UpdateCurrentFontEvent event) {
        log.info("UpdateCurrentFontEvent arrived ");
        optionsManager.updateCurrentFont(event.getFontPath());
        fontManager.reloadCurrentFont();
        srtDisplayerProvider.get().refreshSubtitle();
    }


    @Subscribe
    public void handleUpdateSubtitlePosEvent(UpdateSubtitlePosEvent event) {
        optionsManager.updateSubtitlePos(event.getNewPos());
    }

    @Subscribe
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent event){
        log.debug("switching srt displayer event arrived");
        windowManager.showWindow(mapToWindow(event.getTarget()));
        this.srtDisplayerProvider.setCurrentDisplayer(event.getTarget());
        this.srtDisplayerProvider.get().refreshSubtitle();
    }

    private String mapToWindow(Class<? extends SrtDisplayer> displayer){
        if (displayer.equals(SettingsSrtDisplayer.class))
            return Windows.SETTINGS;
        else
            return Windows.MOVIE;
    }
}
