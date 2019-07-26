package com.youneedsoftware.subtitleBuddy.srt.font.fontManager;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyFileKeys;
import com.youneedsoftware.subtitleBuddy.classpathFileFinder.ClassPathFileFinder;
import com.youneedsoftware.subtitleBuddy.srt.SrtFont;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.*;

@Log4j
@Singleton
public class FontManagerImpl implements SrtFontManager {
    //todo auslagern
    private static final String ITALIC_FILE_SUFFIX = "italic";

    private String userFontPath;
    @Getter
    private double userFontSize;
    @Getter
    private Color userFontColor;

    private ClassPathFileFinder classPathFileFinder;

    @Inject
    public FontManagerImpl(@Named(PropertyFileKeys.USER_DEFAULT_FONT_PATH) String userFontPath, @Named(PropertyFileKeys.USER_FONT_SIZE_KEY) Double userFontSize, @Named(PropertyFileKeys.USER_FONT_COLOR_KEY) String userFontColorString, ClassPathFileFinder classPathFileFinder) {
        this.userFontPath = userFontPath;
        this.userFontSize = userFontSize;
        this.userFontColor = setUserFontColor(userFontColorString);
        this.classPathFileFinder = classPathFileFinder;
    }

    private Color setUserFontColor(String userColorString){
        try {
            return javafx.scene.paint.Color.valueOf(userColorString);
        }catch (Exception e){
            log.warn("user color string: " + userColorString + " was not identified -> using white",e);
            return javafx.scene.paint.Color.WHITE;
        }
    }

    @Override
    public SrtFont loadDefaultFont() {
        return loadDefaultFont(userFontSize);
    }

    @Override
    public SrtFont loadDefaultFont(double fontSize) {
        try {
            checkNotNull(userFontPath);
            checkState(fontSize>0);
            return loadFont(userFontPath,fontSize);
        } catch (Exception e) {
            log.error("Could not load application default font, using System default Font instead ", e);
            return new SrtFont(Font.getDefault(),Font.getDefault());
        }
    }

    @Override
    public void setFontColor(Color color) {
        this.userFontColor=color;
    }

    @Override
    public Color getFontColor() {
        return this.userFontColor;
    }

    @Override
    public SrtFont loadFont(String fontPath, double fontSize) throws SrtFontLoadingException {
        //todo cache implementieren evtl
        try {
            String fileType = FilenameUtils.getExtension(fontPath);
            log.trace("fileType : " + fileType + " of filepath: " + fontPath);
            checkArgument(fileType.equals("ttf") || fileType.equals("otf"));
            File fontFile;
            if(Paths.get(fontPath).isAbsolute()){
                fontFile = new File(fontPath);
            }else {
                fontFile= classPathFileFinder.findFileOnClassPath(fontPath).getFile();
            }
            Font regularFont = Font.loadFont(new FileInputStream(fontFile),fontSize);
            checkNotNull(regularFont);
            String italicFontPath = fontPath.substring(0,fontPath.length()-3)+ITALIC_FILE_SUFFIX+"."+fileType;
            log.trace("italicFontName = " + italicFontPath);
            File italicFontFile;
            if(Paths.get(fontPath).isAbsolute()){
                italicFontFile = new File(italicFontPath);
            }else {
                italicFontFile = classPathFileFinder.findFileOnClassPath(italicFontPath).getFile();
            }
            if(!italicFontFile.exists()){
                throw new SrtFontLoadingException("Font with name: " + italicFontPath + " not found");
            }
            Font italicFont = Font.loadFont(new FileInputStream(italicFontFile),fontSize);
            checkNotNull(italicFont);
            return new SrtFont(regularFont,italicFont);
        }catch (Exception e){
            throw new SrtFontLoadingException(e);
        }
    }
}
