open module io.github.vincemann.subtitlebuddy {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    // needs to be here
    requires org.apache.commons.logging;
    requires org.apache.commons.configuration2;
    requires com.github.kwhat.jnativehook;

    requires java.compiler;
    requires java.naming;
    requires org.apache.logging.log4j;

    requires jakarta.inject;
    requires org.apache.commons.io;
    requires com.google.guice;
    requires com.google.common;

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
    exports io.github.vincemann.subtitlebuddy.gui.event;
    exports io.github.vincemann.subtitlebuddy.gui.movie;
    exports io.github.vincemann.subtitlebuddy.gui.settings;
    exports io.github.vincemann.subtitlebuddy.gui.options;

    exports io.github.vincemann.subtitlebuddy.listeners;
    exports io.github.vincemann.subtitlebuddy.listeners.key;
    exports io.github.vincemann.subtitlebuddy.listeners.mouse;

    exports io.github.vincemann.subtitlebuddy.module;
    exports io.github.vincemann.subtitlebuddy.options;
    exports io.github.vincemann.subtitlebuddy.events;
    exports io.github.vincemann.subtitlebuddy.config;
    exports io.github.vincemann.subtitlebuddy.config.strings;
    exports io.github.vincemann.subtitlebuddy.cp;

    exports io.github.vincemann.subtitlebuddy.util;
    exports io.github.vincemann.subtitlebuddy.util.fx;
    exports io.github.vincemann.subtitlebuddy.util.vec;
    exports io.github.vincemann.subtitlebuddy.gui;
}