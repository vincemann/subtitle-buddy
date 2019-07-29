package io.github.vincemann.subtitleBuddy.gui.pages;

import io.github.vincemann.subtitleBuddy.gui.GuiTest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
abstract class AbstractPage {
    private GuiTest driver;
}
