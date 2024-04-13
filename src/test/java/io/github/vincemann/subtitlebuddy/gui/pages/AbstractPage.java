package io.github.vincemann.subtitlebuddy.gui.pages;

import io.github.vincemann.subtitlebuddy.gui.GuiTest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
abstract class AbstractPage {
    private GuiTest driver;
}
