package io.github.vincemann.subtitlebuddy.events;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ToggleNextClickHotkeyEvent {
    private boolean enabled;
}
