package io.github.vincemann.subtitlebuddy.srt;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.options.Options;
import io.github.vincemann.subtitlebuddy.options.OptionsManager;

@Singleton
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
