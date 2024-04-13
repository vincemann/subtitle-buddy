package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class SrtFontChangeEvent {

    @NotNull
    private SrtFonts srtFonts;

    @NotNull
    private String fontPath;
}
