package io.github.vincemann.subtitlebuddy.srt;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.*;

import java.nio.file.Path;

@Getter
@ToString
@EqualsAndHashCode
public class FontBundle {
    private Font regularFont;
    private Font italicFont;
    private String regularFileName;
    private String italicFileName;

    public FontBundle(Font regularFont, Font italicFont, String regularFileName, String italicFileName) {
        this.regularFont = regularFont;
        this.italicFont = italicFont;
        this.regularFileName = regularFileName;
        this.italicFileName = italicFileName;
    }

    /**
     * Creates a new FontBundle with the specified size for both regular and italic fonts.
     *
     * @param size The new size for the fonts.
     * @return A new FontBundle instance with fonts of the specified size.
     */
    public FontBundle withSize(double size) {
        Font resizedRegular = Font.font(regularFont.getFamily(), size);
        Font resizedItalic = Font.font(italicFont.getFamily(), size);
        return new FontBundle(resizedRegular, resizedItalic, regularFileName, italicFileName);
    }

    // used for displaying entries in font choice box
    @Override
    public String toString() {
        return regularFileName;
    }
}
