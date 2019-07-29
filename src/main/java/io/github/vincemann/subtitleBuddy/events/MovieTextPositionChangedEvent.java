package io.github.vincemann.subtitleBuddy.events;

import io.github.vincemann.subtitleBuddy.util.vec2d.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class MovieTextPositionChangedEvent {
    @NotNull
    private Vector2D newPos;
}
