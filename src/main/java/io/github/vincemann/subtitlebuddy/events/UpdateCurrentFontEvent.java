package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
@Getter
public class UpdateCurrentFontEvent {
    private String fontPath;
}
