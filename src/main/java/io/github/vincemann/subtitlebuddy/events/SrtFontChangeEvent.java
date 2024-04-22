package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
@Getter
public class SrtFontChangeEvent {

    
    private SrtFonts srtFonts;

    
    private String fontPath;
}
