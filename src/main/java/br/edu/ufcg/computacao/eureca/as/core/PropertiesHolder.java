package br.edu.ufcg.computacao.eureca.as.core;

import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.as.core.util.HomeDir;
import br.edu.ufcg.computacao.eureca.as.core.util.PropertiesUtil;

import java.util.Properties;

public class PropertiesHolder {
    private Properties properties;
    private static PropertiesHolder instance;

    private PropertiesHolder() throws FatalErrorException {
        String path = HomeDir.getPath();
        String configFileName = path + SystemConstants.AS_CONF_FILE_NAME;
        this.properties = PropertiesUtil.readProperties(configFileName);
    }

    public static synchronized PropertiesHolder getInstance() throws FatalErrorException {
        if (instance == null) {
            instance = new PropertiesHolder();
        }
        return instance;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String getProperty(String propertyName, String defaultPropertyValue) {
        String propertyValue = this.properties.getProperty(propertyName, defaultPropertyValue);
        if (propertyValue.trim().isEmpty()) {
            propertyValue = defaultPropertyValue;
        }
        return propertyValue;
    }

    public Properties getProperties() {
        return this.properties;
    }
}
