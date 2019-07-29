package io.github.vincemann.subtitleBuddy.srt.font.fontManager;

import io.github.vincemann.subtitleBuddy.srt.SrtFont;
import javafx.scene.paint.Color;


import java.net.MalformedURLException;

public interface SrtFontManager {

    /**
     * It is expected, that next to /foo/filename.ttf there coexists /foo/filename.italic.ttf
     * otherwise SrtFontLoadingException gets trown
     *
     * supported Types = ttf, otf
     * @param path
     * @param fontSize
     * @return
     * @throws MalformedURLException
     * @throws IllegalArgumentException if unsupported type was supplied
     * @throws SrtFontLoadingException
     */
    public SrtFont loadFont(String path, double fontSize) throws MalformedURLException, SrtFontLoadingException;


    public SrtFont loadDefaultFont();

    public SrtFont loadDefaultFont(double fontSize);

    public Color getFontColor();

    public void setFontColor(Color color);

    public double getUserFontSize();
}