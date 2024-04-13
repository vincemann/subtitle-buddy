package io.github.vincemann.subtitlebuddy.config.properties;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ApacheConfigFileTest extends AbstractApacheConfigFileTest {

    @Test
    public void testSaveProperty() throws FileNotFoundException, PropertyAccessException {
        String testKey = "abc";
        String testValue = "i rock";
        getEmptyTestPropertiesFile().saveProperty(testKey,testValue);

        Assert.assertTrue(isPropertyInFile(testKey,testValue));
        String result = getEmptyTestPropertiesFile().getString(testKey);
        Assert.assertNotNull(result);
        Assert.assertEquals(testValue,result);
    }

}
