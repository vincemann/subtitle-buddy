package io.github.vincemann.subtitlebuddy.options;

import com.google.inject.Inject;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import javafx.scene.paint.Color;

public class FontOptions {
    private Options options;
    private OptionsManager manager;

    @Inject
    public FontOptions(Options options, OptionsManager manager) {
        this.options = options;
        this.manager = manager;
    }

    public String getCurrentFontPath() {
        return options.getCurrentFontPath();
    }

    public Color getFontColor() {
        return options.getFontColor();
    }

    public void updateCurrentFontPath(String path){
        manager.updateCurrentFontPath(path);
    }

    public void updateFontColor(Color color){
        manager.updateFontColor(color);
    }

    public void updateDefaultFont(FontBundle fonts){
        manager.updateDefaultFont(fonts);
    }

}
