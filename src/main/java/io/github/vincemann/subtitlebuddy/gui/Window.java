package io.github.vincemann.subtitlebuddy.gui;

import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@AllArgsConstructor
@Getter
public class Window {

    private String name;
    private Stage stage;
    private Object controller;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Window window)) return false;

        return new EqualsBuilder().append(getName(), window.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }
}
