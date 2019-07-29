package io.github.vincemann.subtitleBuddy.guice;

import com.google.inject.Injector;
import io.github.vincemann.subtitleBuddy.main.Main;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Integrationtests should implement this interface in order to get the application injector
 */
public interface IntegrationTest {

    default Injector getApplicationInjector(){
        return checkNotNull(Main.getInjector());
    }
}
