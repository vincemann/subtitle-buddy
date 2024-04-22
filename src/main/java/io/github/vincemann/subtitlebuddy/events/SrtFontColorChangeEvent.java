package io.github.vincemann.subtitlebuddy.events;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;




@AllArgsConstructor
@Getter
public class SrtFontColorChangeEvent {
    private Color color;
}
