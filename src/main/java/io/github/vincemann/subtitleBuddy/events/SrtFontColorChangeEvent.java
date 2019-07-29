package io.github.vincemann.subtitleBuddy.events;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@Getter
public class SrtFontColorChangeEvent {
    @NotNull
    private Color color;
}
