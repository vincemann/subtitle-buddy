package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

/**
 * Used to keep options object and disk state in form of {@link PropertiesFile} in sync.
 * Offers update methods for options and method for parsing the options from underlying {@link PropertiesFile}.
 */
// todo should split this up into FontOptionsManager, SrtOptionsManager and so on - this is an acceptable middle way tho, bc at least each domain has own options
@Log4j2
@Singleton
public class OptionsManagerImpl implements OptionsManager {

    private Options options;
    private PropertiesFile properties;

    private EventBus eventBus;

    @Inject
    public OptionsManagerImpl(PropertiesFile propertiesFile, EventBus eventBus) {
        this.properties = propertiesFile;
        this.eventBus = eventBus;
    }

    /**
     * Parse properties file and create Options
     */
    @Override
    public Options parseOptions() {
        Options options = new Options();

        String currentFont = properties.getString(PropertyFileKeys.USER_CURRENT_FONT);
        options.setCurrentFont(currentFont);

        String sColor = properties.getString(PropertyFileKeys.USER_FONT_COLOR);
        options.setFontColor(Color.valueOf(sColor));

        String subtitlePos = properties.getString(PropertyFileKeys.SUBTITLE_POS);
        Vector2D pos = new Vector2D(subtitlePos);
        options.setSubtitlePosition(pos);

        boolean endMovieModeHotkeyEnabled = properties.getBoolean(PropertyFileKeys.END_MOVIE_MODE_HOTKEY_ENABLED);
        options.setEndMovieModeHotkeyEnabled(endMovieModeHotkeyEnabled);

        boolean spaceHotkeyEnabled = properties.getBoolean(PropertyFileKeys.SPACE_HOTKEY_ENABLED);
        options.setSpaceHotkeyEnabled(spaceHotkeyEnabled);

        boolean nextClickHotkeyEnabled = properties.getBoolean(PropertyFileKeys.NEXT_CLICK_HOT_KEY_ENABLED);
        options.setNextClickHotkeyEnabled(nextClickHotkeyEnabled);

        String defaultSubtitle = properties.getString(PropertyFileKeys.DEFAULT_SUBTITLE);
        options.setDefaultSubtitle(defaultSubtitle);

        String encoding = properties.getString(PropertyFileKeys.ENCODING);
        options.setEncoding(encoding);

        int movieFontSize = properties.getInt(PropertyFileKeys.MOVIE_FONT_SIZE);
        options.setMovieFontSize(movieFontSize);

        int settingsFontSize = properties.getInt(PropertyFileKeys.SETTINGS_FONT_SIZE);
        options.setSettingsFontSize(settingsFontSize);

        this.options = options;
        return options;
    }

    @Override
    public void updateCurrentFont(String path) {
        options.setCurrentFont(path);

        try {
            properties.saveProperty(PropertyFileKeys.USER_CURRENT_FONT, path);
        } catch (PropertyAccessException e) {
            log.error("could not save current font modification, " + e.getMessage());
        }
    }

    @Override
    public void updateFontColor(Color color) {
        options.setFontColor(color);
        try {
            properties.saveProperty(PropertyFileKeys.USER_FONT_COLOR, color.toString());
        } catch (PropertyAccessException e) {
            log.error("could not save font color modification, " + e.getMessage());
        }
    }

    @Override
    public void updateSubtitlePos(Vector2D pos) {
        options.setSubtitlePosition(pos);
        try {
            properties.saveProperty(PropertyFileKeys.SUBTITLE_POS, pos.toString());
        } catch (PropertyAccessException e) {
            log.error("could not save subtitle pos modification, " + e.getMessage());
        }
    }

    @Override
    public void updateSpaceHotkeyEnabled(boolean value) {
        options.setSpaceHotkeyEnabled(value);
        try {
            properties.saveProperty(PropertyFileKeys.SPACE_HOTKEY_ENABLED, String.valueOf(value));
        } catch (PropertyAccessException e) {
            log.error("could not save space hotkey enabled modification, " + e.getMessage());
        }
    }

    @Override
    public void updateMovieFontSize(int size) {
        options.setMovieFontSize(size);
        try {
            properties.saveProperty(PropertyFileKeys.MOVIE_FONT_SIZE, String.valueOf(size));
        } catch (PropertyAccessException e) {
            log.error("could not save movie font size modification, " + e.getMessage());
        }
    }

    @Override
    public void updateEndMovieModeHotkeyEnabled(boolean value) {
        options.setEndMovieModeHotkeyEnabled(value);

        try {
            properties.saveProperty(PropertyFileKeys.END_MOVIE_MODE_HOTKEY_ENABLED, String.valueOf(value));
        } catch (PropertyAccessException e) {
            log.error("could not save movie font size modification, " + e.getMessage());
        }
    }

    @Override
    public void updateNextClickHotkeyEnabled(boolean value) {
        options.setNextClickHotkeyEnabled(value);
        try {
            properties.saveProperty(PropertyFileKeys.NEXT_CLICK_HOT_KEY_ENABLED, String.valueOf(value));
        } catch (PropertyAccessException e) {
            log.error("could not save next click hotkey enabled modification, " + e.getMessage());
        }
    }


}
