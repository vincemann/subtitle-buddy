open module io.github.vincemann.subtitlebuddy.test {
    requires io.github.vincemann.subtitlebuddy;

    requires javafx.controls;
    requires javafx.fxml;
    requires org.jnativehook;
    requires com.google.common;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.commons.logging;
    requires org.apache.commons.configuration2;
    requires org.apache.logging.log4j;
    requires org.slf4j;
    requires jakarta.inject;

    // Test libraries
    requires junit;
    requires org.testfx;
    requires org.testfx.junit;
}