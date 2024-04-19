open module io.github.vincemann.subtitlebuddy {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;
    requires com.google.common;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.logging;
    requires java.validation;
    requires org.apache.commons.configuration2;
    requires spring.core;
    requires com.github.kwhat.jnativehook;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j;
    requires org.slf4j;
    requires jakarta.inject;


//    opens io.github.vincemann.subtitlebuddy to javafx.fxml;
//    opens io.github.vincemann.subtitlebuddy.gui.srtdisplayer to com.google.guice;
//    opens io.github.vincemann.subtitlebuddy.gui.stages.controller to com.google.guice;


    exports io.github.vincemann.subtitlebuddy;
    exports io.github.vincemann.subtitlebuddy.srt;
    exports io.github.vincemann.subtitlebuddy.srt.engine;
    exports io.github.vincemann.subtitlebuddy.srt.font;
    exports io.github.vincemann.subtitlebuddy.srt.parser;
    exports io.github.vincemann.subtitlebuddy.srt.srtfile;
    exports io.github.vincemann.subtitlebuddy.srt.stopwatch;

    exports io.github.vincemann.subtitlebuddy.gui.dialog;
    exports io.github.vincemann.subtitlebuddy.gui.filechooser;
    exports io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath;
    exports io.github.vincemann.subtitlebuddy.gui.srtdisplayer;
    exports io.github.vincemann.subtitlebuddy.gui.stages;
    exports io.github.vincemann.subtitlebuddy.gui.stages.controller;

    exports io.github.vincemann.subtitlebuddy.listeners;
    exports io.github.vincemann.subtitlebuddy.listeners.key;
    exports io.github.vincemann.subtitlebuddy.listeners.mouse;

    exports io.github.vincemann.subtitlebuddy.module;
    exports io.github.vincemann.subtitlebuddy.properties;
    exports io.github.vincemann.subtitlebuddy.service;
    exports io.github.vincemann.subtitlebuddy.events;
    exports io.github.vincemann.subtitlebuddy.config;
    exports io.github.vincemann.subtitlebuddy.config.strings;
    exports io.github.vincemann.subtitlebuddy.cp;

    exports io.github.vincemann.subtitlebuddy.util;
    exports io.github.vincemann.subtitlebuddy.util.fx;
    exports io.github.vincemann.subtitlebuddy.util.vec;
}