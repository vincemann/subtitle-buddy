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
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.options.OptionsManagerImpl;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Singleton
public class SrtDisplayerEventHandlerImpl implements SrtDisplayerEventHandler {

    private SrtDisplayerProvider srtDisplayerProvider;

    private OptionsManagerImpl optionsManager;

    private WindowManager windowManager;

    @Inject
    public SrtDisplayerEventHandlerImpl(SrtDisplayerProvider srtDisplayerProvider, OptionsManagerImpl optionsManager, WindowManager windowManager) {
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.optionsManager = optionsManager;
        this.windowManager = windowManager;
    }

    @Override
    @Subscribe
    public void handleUpdateFontColorEvent(UpdateFontColorEvent e) {
        log.info("UpdateFontColorEvent arrived ");
        srtDisplayerProvider.get(MovieSrtDisplayer.class).setFontColor(e.getColor());
        log.debug("changing color to: " + e.getColor().toString());
        srtDisplayerProvider.get().displaySubtitle(srtDisplayerProvider.get().getLastSubtitleText());
        optionsManager.updateFontColor(e.getColor().toString());
    }

    @Override
    @Subscribe
    public void handleUpdateCurrentFontEvent(UpdateCurrentFontEvent e) {
        log.info("UpdateCurrentFontEvent arrived ");
        // those will react to optionsUpdated event and ask the fontManager again to load the current font!
//        srtDisplayerProvider.get(MovieSrtDisplayer.class).setCurrentFont(e.getFont());
//        srtDisplayerProvider.get(SettingsSrtDisplayer.class).setCurrentFont(e.getFont());
        srtDisplayerProvider.get().displaySubtitle(srtDisplayerProvider.get().getLastSubtitleText());

        optionsManager.updateCurrentFont(e.getFont().getPath());
        // also save this font as new default font
        fontManager.reloadDefaultFont(e.getFont());
    }

    @Override
    @Subscribe
    public void handleUpdateSubtitlePosEvent(UpdateSubtitlePosEvent e) {
        optionsManager.updateSubtitlePos(e.getNewPos());
    }

    @Subscribe
    @Override
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent e){
        log.debug("switching srt Displayer event getting handled...");
        SubtitleText lastSub = this.srtDisplayerProvider.get().getLastSubtitleText();
        windowManager.showWindow(mapToWindow(e.getTarget()));
        this.srtDisplayerProvider.setCurrentDisplayer(e.getTarget());
        this.srtDisplayerProvider.get().displaySubtitle(lastSub);
    }

    private String mapToWindow(Class<? extends SrtDisplayer> displayer){
        if (displayer.equals(SettingsSrtDisplayer.class))
            return Windows.SETTINGS;
        else
            return Windows.MOVIE;
    }
}
