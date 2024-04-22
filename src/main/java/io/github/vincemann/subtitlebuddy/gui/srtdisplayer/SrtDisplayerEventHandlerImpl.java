package io.github.vincemann.subtitlebuddy.gui.srtdisplayer;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.properties.PropertyAccessException;
import io.github.vincemann.subtitlebuddy.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.events.MovieTextPositionChangedEvent;
import io.github.vincemann.subtitlebuddy.events.SrtFontChangeEvent;
import io.github.vincemann.subtitlebuddy.events.SrtFontColorChangeEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Singleton
public class SrtDisplayerEventHandlerImpl implements SrtDisplayerEventHandler {

    private SrtDisplayerProvider srtDisplayerProvider;
    private PropertiesFile properties;

    @Inject
    public SrtDisplayerEventHandlerImpl(SrtDisplayerProvider srtDisplayerProvider, PropertiesFile properties) {
        this.srtDisplayerProvider = srtDisplayerProvider;
        this.properties = properties;
    }

    @Override
    @Subscribe
    public void handleFontColorChangedEvent(SrtFontColorChangeEvent e) {
        log.info("ColorChangedEvent arrived ");
        srtDisplayerProvider.get(MovieSrtDisplayer.class).setFontColor(e.getColor());
        log.debug("changing color to: " + e.getColor().toString());
        //immediate update
        srtDisplayerProvider.get().displaySubtitle(srtDisplayerProvider.get().getLastSubtitleText());
        try {
            properties.saveProperty(PropertyFileKeys.USER_FONT_COLOR,/*Arrays.asList(e.getColor().getRed(),e.getColor().getGreen(),e.getColor().getBlue()*/e.getColor().toString());
        } catch (PropertyAccessException e1) {
            log.error("could not save color modification, "+ e1.getMessage());
        }
    }

    @Override
    @Subscribe
    public void handleFontChangedEvent(SrtFontChangeEvent e) {
        log.info("FontChangedEvent arrived ");
        srtDisplayerProvider.get(MovieSrtDisplayer.class).setCurrentFont(e.getSrtFonts());
        srtDisplayerProvider.get(SettingsSrtDisplayer.class).setCurrentFont(e.getSrtFonts());
        srtDisplayerProvider.get().displaySubtitle(srtDisplayerProvider.get().getLastSubtitleText());
        try {
            properties.saveProperty(PropertyFileKeys.USER_DEFAULT_FONT,e.getFontPath());
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
            properties.saveProperty(PropertyFileKeys.USER_MOVIE_TEXT_POSITION, decodedVecPos);
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
        this.srtDisplayerProvider.setCurrentDisplayer(e.getSrtDisplayerMode());
        this.srtDisplayerProvider.get().open();
        this.srtDisplayerProvider.get().displaySubtitle(lastSub);
    }
}
