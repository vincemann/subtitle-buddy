package io.github.vincemann.subtitleBuddy.events;

import io.github.vincemann.subtitleBuddy.srt.SrtFont;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class SrtFontChangeEvent {

    @NotNull
    private SrtFont srtFont;

    @NotNull
    private String fontPath;
}
