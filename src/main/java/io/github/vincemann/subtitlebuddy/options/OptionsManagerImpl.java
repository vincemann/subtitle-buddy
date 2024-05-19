package io.github.vincemann.subtitlebuddy.options;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

/**
 * Used to keep options object and disk state in form of {@link PropertiesFile} in sync.
 * Offers update methods for options and method for parsing the options from underlying {@link PropertiesFile}.
 */
@Log4j2
public class OptionsManagerImpl implements OptionsManager {

    private Options options;
    private PropertiesFile properties;

    private EventBus eventBus;

    @Inject
    public OptionsManagerImpl(Options options, PropertiesFile propertiesFile, EventBus eventBus) {
        this.options = options;
        this.properties = propertiesFile;
        this.eventBus = eventBus;
    }

    @Override
    public Options parseOptions(){
        Options options = new Options();
        String sColor = properties.getString(PropertyFileKeys.USER_FONT_COLOR);
        Color color = Color.valueOf(sColor);
        options.setFontColor(color);




    }

    @Override
    public void updateCurrentFontPath(String path){
        options.setCurrentFontPath(path);

        try {
            properties.saveProperty(PropertyFileKeys.USER_DEFAULT_FONT,path);
        }
        catch (PropertyAccessException e1) {
            log.error("could not save default font modification, "+ e1.getMessage());
        }
        eventBus.post(new OptionsUpdatedEvent());
    }

    @Override
    public void updateFontColor(Color color){
        options.setFontColor(color);
        try {
            properties.saveProperty(PropertyFileKeys.USER_FONT_COLOR,color.toString());
        } catch (PropertyAccessException e1) {
            log.error("could not save font color modification, "+ e1.getMessage());
        }
        eventBus.post(new OptionsUpdatedEvent());
    }

    @Override
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
