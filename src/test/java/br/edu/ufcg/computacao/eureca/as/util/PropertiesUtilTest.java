package br.edu.ufcg.computacao.eureca.as.util;

import br.edu.ufcg.computacao.eureca.as.core.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.as.core.util.HomeDir;
import br.edu.ufcg.computacao.eureca.as.core.util.PropertiesUtil;

import org.junit.Test;
import java.util.Properties;
import org.junit.Assert;

public class PropertiesUtilTest {

    private static final String FAKE_PROPERTIES_FILE_NAME = "fake-properties.conf";
    private static final String NON_EXISTENT_FILE_NAME = "non-existent-file-name.conf";

    // test if the loadProperties returns the expected properties when a
    // correct filepath is passed
    @Test
    public void testLoadPropertiesSuccessfully() {
        // setup
        String filename = HomeDir.getPath() + FAKE_PROPERTIES_FILE_NAME;

        // exercise
        Properties props = PropertiesUtil.readProperties(filename);

        // verify
        Assert.assertEquals(props.getProperty("fake_property1"), "f1");
        Assert.assertEquals(props.getProperty("fake_property2"), "f2");
        Assert.assertEquals(props.getProperty("fake_property3"), "f3");
    }

    // test if a FatalErrorException is thrown when a non existent filepath is passed to loadProperties
    @Test(expected = FatalErrorException.class) // verify
    public void testLoadPropertiesWithANonExistentFile() {
        // setup // exercise
        PropertiesUtil.readProperties(NON_EXISTENT_FILE_NAME);
    }


    // Test if it can correctly get properties form a file
    @Test
    public void testReadPropertiesFromSingleFile() {

        // set up
        String fakePropertiesPath = HomeDir.getPath() + FAKE_PROPERTIES_FILE_NAME;
        Properties props = PropertiesUtil.readProperties(fakePropertiesPath);

        // exercise/verify

        Assert.assertEquals(props.getProperty("fake_property1"), "f1");
        Assert.assertEquals(props.getProperty("fake_property2"), "f2");
        Assert.assertEquals(props.getProperty("fake_property3"), "f3");
    }
}