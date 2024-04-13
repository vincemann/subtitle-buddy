package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class MovieTextPositionChangedEvent {
    @NotNull
    private Vector2D newPos;
}
