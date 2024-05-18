package io.github.vincemann.subtitlebuddy.options;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

/**
 * Offers methods for updating options and keeps memory state in sync with disk state.
 */
@Log4j2
public class OptionsManager {

    private Options options;
    private PropertiesFile properties;

    private EventBus eventBus;

    @Inject
    public OptionsManager(Options options, PropertiesFile propertiesFile, EventBus eventBus) {
        this.options = options;
        this.properties = propertiesFile;
        this.eventBus = eventBus;
    }

    public void updateCurrentFont(FontBundle fonts){
        options.setFont(fonts);
        eventBus.post(new OptionsUpdatedEvent());
    }

    public void updateFontColor(Color color){
        options.setFontColor(color);
        try {
            properties.saveProperty(PropertyFileKeys.USER_FONT_COLOR,color.toString());
        } catch (PropertyAccessException e1) {
            log.error("could not save font color modification, "+ e1.getMessage());
        }
        eventBus.post(new OptionsUpdatedEvent());
    }

    public void updateDefaultFont(FontBundle fonts){
        options.setFont(fonts);
        try {
            properties.saveProperty(PropertyFileKeys.USER_DEFAULT_FONT,fonts.getPath());
        }
        catch (PropertyAccessException e1) {
            log.error("could not save default font modification, "+ e1.getMessage());
        }
        eventBus.post(new OptionsUpdatedEvent());
    }

    public void updateSubtitlePos(Vector2D pos){
        options.setSubtitlePosition(pos);
        try {
            properties.saveProperty(PropertyFileKeys.USER_MOVIE_TEXT_POSITION, pos.toString());
        } catch (PropertyAccessException e1) {
            log.error("could not save movieTextPosition modification, " + e1.getMessage());
        }
        // give components that read options the chance to refresh
        eventBus.post(new OptionsUpdatedEvent());
    }


}
