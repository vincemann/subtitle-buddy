package io.github.vincemann.subtitlebuddy.filechooser;

import io.github.vincemann.subtitlebuddy.config.properties.AbstractApacheConfigFileTest;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyAccessException;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyNotFoundException;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.PropertiesFileLastPathRegistry;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LastPathRegistryImplTest extends AbstractApacheConfigFileTest {
    private static final String TEST_LAST_PATH_FILE_PATH = "src/test/resources/pathTestFile";
    private static final String TEST_LAST_PATH_FILE__PARENT = "src"+File.separator+"test"+File.separator+"resources";

    private LastPathRegistry lastPathRegistry;
    private File testFile;

    @Before
    public void init() throws ConfigurationException {
        super.init();
        this.testFile = new File(TEST_LAST_PATH_FILE_PATH);
        this.lastPathRegistry = new PropertiesFileLastPathRegistry(getEmptyTestPropertiesFile());
        try {
            Assert.assertTrue(testFile.createNewFile());
        }catch (IOException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected = PropertyNotFoundException.class)
    public void testGetNonPresentPath(){
        this.lastPathRegistry.getSavedPath();
    }
    @Test
    public void testSaveAndGetPath() throws PropertyAccessException {
        this.lastPathRegistry.savePath(FileUtils.getParentDir(testFile));
        String lastPath = this.lastPathRegistry.getSavedPath();
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
