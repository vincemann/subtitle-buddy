package gui.pages;

import gui.GuiTest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
abstract class AbstractPage {
    private GuiTest driver;
}
