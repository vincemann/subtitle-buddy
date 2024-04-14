package io.github.vincemann.subtitlebuddy.srt.font;

import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import javafx.scene.paint.Color;


import java.net.MalformedURLException;

public interface SrtFontManager {

    /**
     * It is expected, that next for '/foo/filename.ttf' there is a file coexisting '/foo/filename.italic.ttf',
     * otherwise {@link SrtFontLoadingException} is thrown.
     *
     * Supported font types = ttf, otf
     * @param path
     * @param fontSize
     * @return
     * @throws MalformedURLException
     * @throws IllegalArgumentException if unsupported type was supplied
     * @throws SrtFontLoadingException
     */
    SrtFonts loadFont(String path, double fontSize) throws MalformedURLException, SrtFontLoadingException;


    SrtFonts loadDefaultFont();

    Color getFontColor();

    SrtFonts loadDefaultFont(double fontSize);

    void setFontColor(Color color);

    double getUserFontSize();
}