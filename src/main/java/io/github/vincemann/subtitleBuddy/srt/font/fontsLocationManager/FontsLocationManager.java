package io.github.vincemann.subtitleBuddy.srt.font.fontsLocationManager;


import java.nio.file.Path;

public interface FontsLocationManager {


    /**
     * findet und oder created das font dir und populated es ggfs mit den fonts aus der
     * defaultFontClassPathLocation
     * @param userFontPath                   das dir in dem der user angibt seine fonts zu haben
     * @return
     */
    public Path findFontDirectory(Path userFontPath) throws FontsLocationNotFoundException;


}
