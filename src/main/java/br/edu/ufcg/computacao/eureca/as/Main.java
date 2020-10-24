package br.edu.ufcg.computacao.eureca.as;

import br.edu.ufcg.computacao.eureca.as.constants.EurecaAsConstants;
import br.edu.ufcg.computacao.eureca.as.core.ApplicationFacade;
import br.edu.ufcg.computacao.eureca.as.core.PluginInstantiator;
import br.edu.ufcg.computacao.eureca.as.core.PropertiesHolder;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.systemidp.SystemIdentityProviderPlugin;
import br.edu.ufcg.computacao.eureca.as.core.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements ApplicationRunner {
    private final Logger LOGGER = Logger.getLogger(Main.class);

    @Override
    public void run(ApplicationArguments args) {
        try {
            // Setting up asymmetric cryptography
            String publicKeyFilePath = PropertiesHolder.getInstance().getProperty(EurecaAsConstants.PUBLIC_KEY_FILE_PATH);
            String privateKeyFilePath = PropertiesHolder.getInstance().getProperty(EurecaAsConstants.PRIVATE_KEY_FILE_PATH);
            ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(publicKeyFilePath);
            ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privateKeyFilePath);

            // Setting up plugin
            SystemIdentityProviderPlugin systemIdentityProviderPlugin =
                    PluginInstantiator.getSystemIdentityProviderPlugin();
            
            SystemRolePlugin systemRoleProviderPlugin =
                    PluginInstantiator.getSystemRolePlugin();

            // Setting up application facade
            ApplicationFacade applicationFacade = ApplicationFacade.getInstance();
            
            applicationFacade.initializeFogbowTokenGenerator(systemIdentityProviderPlugin, 
                    systemRoleProviderPlugin);
        } catch (FatalErrorException errorException) {
            LOGGER.fatal(errorException.getMessage(), errorException);
            tryExit();
        }
    }

    private void tryExit() {
        if (!Boolean.parseBoolean(System.getenv("SKIP_TEST_ON_TRAVIS")))
            System.exit(1);
    }
}
