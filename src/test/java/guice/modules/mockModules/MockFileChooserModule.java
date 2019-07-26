package guice.modules.mockModules;

import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import com.youneedsoftware.subtitleBuddy.filechooser.FileChooser;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.alertDialog.AlertDialog;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.continueDialog.ContinueDialog;
import com.youneedsoftware.subtitleBuddy.guice.module.FileChooserModule;
import guice.mockedProviders.MockedFileChooserProvider;

public class MockFileChooserModule extends FileChooserModule {


    public MockFileChooserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void initClassBindings() {
        bind(FileChooser.class).toProvider(new MockedFileChooserProvider());
        bind(ContinueDialog.class).toInstance(new ContinueDialog() {
            @Override
            public boolean askUserToContinue(String message) {
                return true;
            }
        });
        bind(AlertDialog.class).toInstance(new AlertDialog() {
            @Override
            public void tellUser(String message) {
                //do nothing
            }
        });
    }
}
