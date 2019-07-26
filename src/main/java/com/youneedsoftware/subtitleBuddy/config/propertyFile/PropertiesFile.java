package com.youneedsoftware.subtitleBuddy.config.propertyFile;

import java.io.File;
import java.util.List;

public interface PropertiesFile {

    int getInt(String key);
    double getDouble(String key);
    String getString(String key);
    boolean getBoolean(String key);
    long getLong(String key);
    List getList(String key);
    void refresh() throws PropertyAccessException;
    void clear();
    boolean isEmpty();
    void save() throws PropertyAccessException;
    File getFile();

    void saveProperty(String key, Object value) throws PropertyAccessException;


}
