package io.github.vincemann.subtitleBuddy.gui.srtDisplayer;

import io.github.vincemann.subtitleBuddy.srt.SrtFont;
import io.github.vincemann.subtitleBuddy.srt.SubtitleText;
import io.github.vincemann.subtitleBuddy.util.FxUtils.FontUtils;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public interface SrtDisplayer {

    default void adjustTextSize(Text text, int fontSize) {
        if (text.getFont().getSize() != fontSize) {
            String newStyle = FontUtils.resizeStyle(text.getStyle(), fontSize);
            text.setStyle(newStyle);
        }
    }

    public void displayNextClickCounts();

    public void hideNextClickCounts();

    public void displaySubtitle(SubtitleText subtitleText);

    public SrtFont getCurrentFont();

    public void setCurrentFont(SrtFont font);

    public Color getFontColor();

    public void setFontColor(Color color);

    public SubtitleText getLastSubtitleText();

    /**
     * called when SrtDisplayer gets switched, so this should be closed
     * aka made invisible
     */
    public void close();

    /**
     * counterpart to close
     */
    public void open();

    public boolean isDisplaying();

}
