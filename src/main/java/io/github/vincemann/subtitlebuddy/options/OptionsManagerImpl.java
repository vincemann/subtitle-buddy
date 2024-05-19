package io.github.vincemann.subtitlebuddy.options;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
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

        String currentFont = properties.getString(PropertyFileKeys.USER_CURRENT_FONT);
        options.setCurrentFont(currentFont);

        String sColor = properties.getString(PropertyFileKeys.USER_FONT_COLOR);
        Color color = Color.valueOf(sColor);
        options.setFontColor(color);

        String subtitlePos = properties.getString(PropertyFileKeys.SUBTITLE_POS);
        Vector2D pos = new Vector2D(subtitlePos);
        options.setSubtitlePosition(pos);

        boolean spaceHotkeyEnabled = properties.getBoolean(PropertyFileKeys.SPACE_HOTKEY_ENABLED);
        options.setSpaceHotkeyEnabled(spaceHotkeyEnabled);

        boolean nextClickHotkeyEnabled = properties.getBoolean(PropertyFileKeys.NEXT_CLICK_HOT_KEY_ENABLED);
        options.setNextClickHotkeyEnabled(nextClickHotkeyEnabled);

        String defaultSubtitle = properties.getString(PropertyFileKeys.DEFAULT_SUBTITLE);
        options.setDefaultSubtitle(defaultSubtitle);

        String encoding = properties.getString(PropertyFileKeys.ENCODING);
        options.setEncoding(encoding);

        return options;
    }

    @Override
    public void updateCurrentFont(String path){
        options.setCurrentFont(path);

        try {
            properties.saveProperty(PropertyFileKeys.USER_CURRENT_FONT,path);
        }
        catch (PropertyAccessException e1) {
            log.error("could not save current font modification, "+ e1.getMessage());
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
            properties.saveProperty(PropertyFileKeys.SUBTITLE_POS, pos.toString());
        } catch (PropertyAccessException e1) {
            log.error("could not save subtitle pos modification, " + e1.getMessage());
        }
        // give components that read options the chance to refresh
        eventBus.post(new OptionsUpdatedEvent());
    }

    @Override
    public void updateSpaceHotkeyEnabled(boolean value){
        options.setSpaceHotkeyEnabled(value);
        try {
            properties.saveProperty(PropertyFileKeys.SPACE_HOTKEY_ENABLED, String.valueOf(value));
        } catch (PropertyAccessException e1) {
            log.error("could not save space hotkey enabled modification, " + e1.getMessage());
        }
        // give components that read options the chance to refresh
        eventBus.post(new OptionsUpdatedEvent());
    }

    @Override
    public void updateNextClickHotkeyEnabled(boolean value){
        options.setNextClickHotkeyEnabled(value);
        try {
            properties.saveProperty(PropertyFileKeys.NEXT_CLICK_HOT_KEY_ENABLED, String.valueOf(value));
        } catch (PropertyAccessException e1) {
            log.error("could not save next click hotkey enabled modification, " + e1.getMessage());
        }
        // give components that read options the chance to refresh
        eventBus.post(new OptionsUpdatedEvent());
    }




}
