package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Getter;



@AllArgsConstructor
@Getter
public class UpdateSubtitlePosEvent {
    
    private Vector2D newPos;
}
