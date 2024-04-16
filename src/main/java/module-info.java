module hellomodule {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;
    requires com.github.kwhat.jnativehook;
    requires com.google.common;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.logging;
    requires java.validation;
    requires org.apache.commons.configuration2;
    requires spring.core;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j.slf4j;
    requires org.slf4j;
    requires jakarta.inject;

    opens io.github.vincemann.subtitlebuddy to javafx.fxml;
    exports io.github.vincemann.subtitlebuddy;
}