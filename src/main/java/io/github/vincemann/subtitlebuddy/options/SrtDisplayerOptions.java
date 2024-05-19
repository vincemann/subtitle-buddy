package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;

public class SrtDisplayerOptions {

    private Options options;
    private OptionsManager manager;


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
