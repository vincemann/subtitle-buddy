package io.github.vincemann.subtitlebuddy.options;

/**
 * Give me a properties file and I put that into a nice typesafe Options object.
 */
public interface OptionsParser {
    Options parse(PropertiesFile propertiesFile);
}
