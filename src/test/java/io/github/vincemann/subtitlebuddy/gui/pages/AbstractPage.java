package io.github.vincemann.subtitlebuddy.gui.pages;

import io.github.vincemann.subtitlebuddy.gui.GuiTest;
abstract class AbstractPage {
    private GuiTest driver;

    public AbstractPage(GuiTest driver) {
        this.driver = driver;
    }

    public GuiTest getDriver() {
        return driver;
    }
}
