package io.github.vincemann.subtitlebuddy.font;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.options.Options;
import io.github.vincemann.subtitlebuddy.options.OptionsManager;
import javafx.scene.paint.Color;

@Singleton
public class FontOptions {
    private Options options;
    private OptionsManager manager;

    @Inject
    public FontOptions(Options options, OptionsManager manager) {
        this.options = options;
        this.manager = manager;
    }

    public String getCurrentFont() {
        return options.getCurrentFont();
    }

    public Color getFontColor() {
        return options.getFontColor();
    }

    public void updateCurrentFont(String fileName){
        manager.updateCurrentFont(fileName);
    }

    public void updateFontColor(Color color){
        manager.updateFontColor(color);
    }


}
