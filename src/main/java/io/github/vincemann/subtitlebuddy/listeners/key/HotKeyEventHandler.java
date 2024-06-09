package io.github.vincemann.subtitlebuddy.listeners.key;

import com.google.common.eventbus.Subscribe;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;

/**
 * This interface is only here to make sure someone implements the handlers.
 */
public interface HotKeyEventHandler {


    void handleChangeDefaultSubtitleVisibilityEvent(DefaultSubtitleVisibilityHotKeyPressedEvent event) throws TimeStampOutOfBoundsException;

    void handleNextClickHotKeyPressedEvent(NextClickHotkeyPressedEvent event);

    void handleSpaceHotKeyPressedEvent(SpaceHotkeyPressedEvent event);

    void handleMovieModeHotKeyPressedEvent(EndMovieModeHotkeyPressedEvent event);

    @Subscribe
    void handleToggleSpaceHotKeyEvent(ToggleSpaceHotkeyEvent event);

    @Subscribe
    void handleToggleNextClickHotKeyEvent(ToggleNextClickHotkeyEvent event);

    @Subscribe
    void handleToggleBackViaEscEvent(ToggleEndMovieModeHotkeyEvent event);
}
