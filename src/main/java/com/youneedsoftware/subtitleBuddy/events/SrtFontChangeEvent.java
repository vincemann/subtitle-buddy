package com.youneedsoftware.subtitleBuddy.events;

import com.youneedsoftware.subtitleBuddy.srt.SrtFont;
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
