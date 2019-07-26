package config;

import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyAccessException;
import com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler.LastPathHandlerImpl;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidParentDirectoryException;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyNotFoundException;
import com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler.LastPathHandler;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LastPathHandlerImplTest extends AbstractConfigFileTest {
    private static final String TEST_LAST_PATH_FILE_PATH = "src/test/resources/pathTestFile";
    private static final String TEST_LAST_PATH_FILE__PARENT = "src"+File.separator+"test"+File.separator+"resources";

    private LastPathHandler lastPathHandler;
    private File testFile;

    @Before
    public void init() throws ConfigurationException {
        super.init();
        this.testFile = new File(TEST_LAST_PATH_FILE_PATH);
        this.lastPathHandler = new LastPathHandlerImpl(getEmptyTestPropertiesFile());
        try {
            Assert.assertTrue(testFile.createNewFile());
        }catch (IOException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = PropertyNotFoundException.class)
    public void testGetNonPresentPath(){
        this.lastPathHandler.getSavedPath();
    }
    @Test
    public void testSaveAndGetPath() throws InvalidParentDirectoryException, PropertyAccessException {
        this.lastPathHandler.savePathOfParentDir(testFile);
        String lastPath = this.lastPathHandler.getSavedPath();
        Assert.assertEquals(TEST_LAST_PATH_FILE__PARENT,lastPath);
    }

    @After
    public void cleanUp() throws ConfigurationException {
        File testConfigFile = new File(TEST_LAST_PATH_FILE_PATH);
        if(testConfigFile.exists()){
            boolean result = testConfigFile.delete();
            Assert.assertTrue(result);
        }
        super.cleanUp();
    }
}
