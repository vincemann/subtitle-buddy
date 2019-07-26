package guice;

import com.google.inject.Injector;
import com.youneedsoftware.subtitleBuddy.main.Main;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Integrationtests should implement this interface in order to get the application injector
 */
public interface IntegrationTest {

    default Injector getApplicationInjector(){
        return checkNotNull(Main.getInjector());
    }
}
