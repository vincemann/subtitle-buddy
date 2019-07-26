package com.youneedsoftware.subtitleBuddy.gui.srtDisplayer;

import com.google.common.eventbus.Subscribe;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyAccessException;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyFileKeys;
import com.youneedsoftware.subtitleBuddy.events.MovieTextPositionChangedEvent;
import com.youneedsoftware.subtitleBuddy.events.SrtFontChangeEvent;
import com.youneedsoftware.subtitleBuddy.events.SrtFontColorChangeEvent;
import com.youneedsoftware.subtitleBuddy.events.SwitchSrtDisplayerEvent;
import com.youneedsoftware.subtitleBuddy.srt.SubtitleText;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;

@Log4j
@Singleton
public class SrtDisplayerEventHandlerImpl implements SrtDisplayerEventHandler {

    private SrtDisplayerProvider srtDisplayerProvider;
    private PropertiesFile propertiesFile;

    @Inject
    public SrtDisplayerEventHandlerImpl(SrtDisplayerProvider srtDisplayerProvider, PropertiesFile propertiesFile) {
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.propertiesFile = propertiesFile;
    }

    @Override
    @Subscribe
    public void handleFontColorChangedEvent(@Valid SrtFontColorChangeEvent e) {
        log.info("ColorChangedEvent arrived ");
        srtDisplayerProvider.get(MovieSrtDisplayer.class).setFontColor(e.getColor());
        log.debug("changing color to: " + e.getColor().toString());
        //immediate update
        srtDisplayerProvider.get().displaySubtitle(srtDisplayerProvider.get().getLastSubtitleText());
        try {
            propertiesFile.saveProperty(PropertyFileKeys.USER_FONT_COLOR_KEY,/*Arrays.asList(e.getColor().getRed(),e.getColor().getGreen(),e.getColor().getBlue()*/e.getColor().toString());
        } catch (PropertyAccessException e1) {
            log.error("could not save color modification, "+ e1.getMessage());
        }
    }

    @Override
    @Subscribe
    public void handleFontChangedEvent(SrtFontChangeEvent e) {
        log.info("FontChangedEvent arrived ");
        srtDisplayerProvider.get(MovieSrtDisplayer.class).setCurrentFont(e.getSrtFont());
        srtDisplayerProvider.get(SettingsSrtDisplayer.class).setCurrentFont(e.getSrtFont());
        srtDisplayerProvider.get().displaySubtitle(srtDisplayerProvider.get().getLastSubtitleText());
        try {
            propertiesFile.saveProperty(PropertyFileKeys.USER_DEFAULT_FONT_PATH,e.getFontPath());
        }
        catch (PropertyAccessException e1) {
            log.error("could not save path modification, "+ e1.getMessage());
        }
    }

    @Override
    @Subscribe
    public void handleMovieTextPositionChangedEvent(MovieTextPositionChangedEvent e) {
        String decodedVecPos = e.getNewPos().toString();
        log.debug("saving vector position as String in configfile: " + decodedVecPos);

        try {
            propertiesFile.saveProperty(PropertyFileKeys.USER_MOVIE_TEXT_POSITION_KEY, decodedVecPos);
        } catch (PropertyAccessException e1) {
            log.error("could not save movieTextPosition modification, " + e1.getMessage());
        }
    }

    @Subscribe
    @Override
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent e){
        log.debug("switching srt Displayer event getting handled...");
        SubtitleText lastSub = this.srtDisplayerProvider.get().getLastSubtitleText();
        this.srtDisplayerProvider.get().close();
        this.srtDisplayerProvider.setSrtDisplayerMode(e.getSrtDisplayerMode());
        this.srtDisplayerProvider.get().open();
        this.srtDisplayerProvider.get().displaySubtitle(lastSub);
    }
}
