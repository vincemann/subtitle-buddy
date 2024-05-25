package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsDisplayer;
import lombok.extern.log4j.Log4j2;

/**
 * All options write operations are implemented via events so components dont depend on {@link OptionsManager} directly
 * just on their respective domain options for reading (like {@link io.github.vincemann.subtitlebuddy.font.FontOptions}).
 *
 * Also like this I can do other stuff on options updates.
 */
@Log4j2
@Singleton
public class OptionsEventHandler {

    private FontManager fontManager;
    private OptionsManager optionsManager;

    private EventBus eventBus;

    private OptionsDisplayer optionsDisplayer;

    @Inject
    public OptionsEventHandler(FontManager fontManager, OptionsManager optionsManager, EventBus eventBus, OptionsDisplayer optionsDisplayer) {
        this.fontManager = fontManager;
        this.optionsManager = optionsManager;
        this.eventBus = eventBus;
        this.optionsDisplayer = optionsDisplayer;
    }

    @Subscribe
    public void handleUpdateMovieFontSizeEvent(UpdateMovieFontSizeEvent event) {
        log.debug("UpdateFontColorEvent arrived: " + event.getFontSize());
        optionsManager.updateMovieFontSize(event.getFontSize());
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    @Subscribe
    public void handleUpdateFontColorEvent(UpdateFontColorEvent event) {
        log.debug("UpdateFontColorEvent arrived: " + event.getColor().toString());
        optionsManager.updateFontColor(event.getColor());
        eventBus.post(new RequestSubtitleUpdateEvent());
        optionsDisplayer.updatePreview();
    }

    @Subscribe
    public void handleUpdateCurrentFontEvent(UpdateCurrentFontEvent event) {
        log.debug("UpdateCurrentFontEvent arrived: " + event.getFontPath());
        optionsManager.updateCurrentFont(event.getFontPath());
        fontManager.reloadCurrentFont();
        eventBus.post(new RequestSubtitleUpdateEvent());
        optionsDisplayer.updatePreview();
    }


    @Subscribe
    public void handleUpdateSubtitlePosEvent(UpdateSubtitlePosEvent event) {
        log.debug("UpdateSubtitlePosEvent arrived: " + event.getNewPos());
        optionsManager.updateSubtitlePos(event.getNewPos());
    }
}
