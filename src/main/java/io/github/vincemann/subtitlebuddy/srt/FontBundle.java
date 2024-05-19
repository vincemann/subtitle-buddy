package io.github.vincemann.subtitlebuddy.srt;

import javafx.scene.text.Font;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.nio.file.Path;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class FontBundle {
    private Font regularFont;
    private Font italicFont;
    private String regularFileName;
    private String italicFileName;

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
}
