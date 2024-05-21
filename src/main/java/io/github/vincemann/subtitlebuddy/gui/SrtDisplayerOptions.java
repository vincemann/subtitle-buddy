package io.github.vincemann.subtitlebuddy.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.options.Options;
import io.github.vincemann.subtitlebuddy.options.OptionsManager;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;

@Singleton
public class SrtDisplayerOptions {

    private Options options;
    private OptionsManager manager;

    @Inject
    public SrtDisplayerOptions(Options options, OptionsManager manager) {
        this.options = options;
        this.manager = manager;
    }

    public Boolean getNextClickHotkeyEnabled() {
        return options.getNextClickHotkeyEnabled();
    }

    public int getSettingsFontSize() {
        return options.getSettingsFontSize();
    }

    public int getMovieFontSize() {
        return options.getMovieFontSize();
    }


    public Boolean getSpaceHotkeyEnabled() {
        return options.getSpaceHotkeyEnabled();
    }

    public Vector2D getSubtitlePosition() {
        return options.getSubtitlePosition();
    }

    public void updateSubtitlePosition(Vector2D pos){
        manager.updateSubtitlePos(pos);
    }

    public void updateSpaceHotkeyEnabled(boolean value){
        manager.updateSpaceHotkeyEnabled(value);
    }

    public void updateNextClickHotkeyEnabled(boolean value){
        manager.updateNextClickHotkeyEnabled(value);
    }
}
