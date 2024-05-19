package io.github.vincemann.subtitlebuddy.options;

import com.google.inject.Inject;

public class SrtOptions {

    private Options options;
    private OptionsManager manager;

    @Inject
    public SrtOptions(Options options, OptionsManager manager) {
        this.options = options;
        this.manager = manager;
    }

    public String getDefaultSubtitle() {
        return options.getDefaultSubtitle();
    }

    public String getEncoding() {
        return options.getEncoding();
    }

}
