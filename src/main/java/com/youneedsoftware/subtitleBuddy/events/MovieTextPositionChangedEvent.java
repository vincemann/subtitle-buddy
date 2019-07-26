package com.youneedsoftware.subtitleBuddy.events;

import com.youneedsoftware.subtitleBuddy.util.vec2d.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class MovieTextPositionChangedEvent {
    @NotNull
    private Vector2D newPos;
}
