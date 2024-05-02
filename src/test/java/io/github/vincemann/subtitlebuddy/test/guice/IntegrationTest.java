package io.github.vincemann.subtitlebuddy.test.guice;

import com.google.inject.Injector;
import io.github.vincemann.subtitlebuddy.Main;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Integration tests should implement this interface in order to get the application injector
 */
public interface IntegrationTest {

    default Injector getApplicationInjector(){
        return checkNotNull(Main.getInjector());
    }
}
