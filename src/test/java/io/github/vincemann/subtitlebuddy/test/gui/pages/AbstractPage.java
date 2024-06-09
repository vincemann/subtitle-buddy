package io.github.vincemann.subtitlebuddy.test.gui.pages;

import io.github.vincemann.subtitlebuddy.test.gui.GuiTest;
abstract class AbstractPage {
    private GuiTest driver;

    public AbstractPage(GuiTest driver) {
        this.driver = driver;
    }

    public GuiTest getDriver() {
        return driver;
    }
}
