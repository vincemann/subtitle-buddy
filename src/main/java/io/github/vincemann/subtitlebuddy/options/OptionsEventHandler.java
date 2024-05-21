package io.github.vincemann.subtitlebuddy.options;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
public class OptionsEventHandler {

    private FontManager fontManager;
    private OptionsManager optionsManager;

    private EventBus eventBus;

    @Inject
    public OptionsEventHandler(FontManager fontManager, OptionsManager optionsManager, EventBus eventBus) {
        this.fontManager = fontManager;
        this.optionsManager = optionsManager;
        this.eventBus = eventBus;
    }

    @Subscribe
    public void handleUpdateMovieFontSizeEvent(UpdateMovieFontSizeEvent event) {
        log.info("UpdateFontColorEvent arrived ");
        log.debug("updating movie font size to: " + event.getFontSize());
        optionsManager.updateMovieFontSize(event.getFontSize());
        eventBus.post(new RequestSubtitleUpdateEvent());
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
}
