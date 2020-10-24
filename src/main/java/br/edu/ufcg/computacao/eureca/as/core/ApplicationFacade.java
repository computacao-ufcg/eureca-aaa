package br.edu.ufcg.computacao.eureca.as.core;

import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyDefaults;
import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.EurecaAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.systemidp.EurecaTokenGenerator;
import br.edu.ufcg.computacao.eureca.as.core.systemidp.SystemIdentityProviderPlugin;
import br.edu.ufcg.computacao.eureca.as.core.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.PropertiesUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;

import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

public class ApplicationFacade {
    private final Logger LOGGER = Logger.getLogger(ApplicationFacade.class);

    private static ApplicationFacade instance;

    private String buildNumber;

    private EurecaTokenGenerator eurecaTokenGenerator;

    private ApplicationFacade() {
        this.buildNumber = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                ConfigurationPropertyDefaults.BUILD_NUMBER);
    }

    public static ApplicationFacade getInstance() {
        synchronized (ApplicationFacade.class) {
            if (instance == null) {
                instance = new ApplicationFacade();
            }
            return instance;
        }
    }

    public void initializeEurecaTokenGenerator(SystemIdentityProviderPlugin systemIdentityProviderPlugin,
                                               SystemRolePlugin systemRoleProviderPlugin) {
        // The token generator plugin generates a raw token; the wrapper adds an expiration time,
        // a signature, and encrypts the token using the public key provided by the client.
        this.eurecaTokenGenerator = new EurecaTokenGenerator(systemIdentityProviderPlugin,
                systemRoleProviderPlugin);
    }

    public String createToken(Map<String, String> userCredentials, String publicKey) throws EurecaAsException {
        // There is no need to authenticate the user or authorize this operation
        return this.eurecaTokenGenerator.createToken(userCredentials, publicKey);
    }

    public String getPublicKey() throws InternalServerErrorAsException {
        // There is no need to authenticate the user or authorize this operation
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new InternalServerErrorAsException(e.getMessage());
        }
    }

    public String getVersionNumber() {
        // There is no need to authenticate the user or authorize this operation
        return SystemConstants.API_VERSION_NUMBER + "-" + this.buildNumber;
    }

    // Used for testing
    protected void setBuildNumber(String fileName) {
        Properties properties = PropertiesUtil.readProperties(fileName);
        this.buildNumber = properties.getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                ConfigurationPropertyDefaults.BUILD_NUMBER);
    }
}
