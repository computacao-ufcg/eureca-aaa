package br.edu.ufcg.computacao.eureca.as.core.systemidp;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.EurecaAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.util.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class EurecaTokenGenerator {
    private static final Logger LOGGER = Logger.getLogger(EurecaTokenGenerator.class);

    private SystemIdentityProviderPlugin embeddedPlugin;

    private RSAPrivateKey privateKey;

    private SystemRolePlugin systemRoleProvider;
    
    public EurecaTokenGenerator(SystemIdentityProviderPlugin embeddedPlugin,
                                SystemRolePlugin systemRoleProvider) {
        this.embeddedPlugin = embeddedPlugin;
        try {
            this.privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (InternalServerErrorAsException e) {
            throw new FatalErrorException(Messages.ERROR_READING_PRIVATE_KEY_FILE, e);
        }
        this.systemRoleProvider = systemRoleProvider;
    }

    public String createToken(Map<String, String> userCredentials, String publicKeyString) throws EurecaAsException {
        SystemUser systemUser = this.embeddedPlugin.getSystemUser(userCredentials);
        systemRoleProvider.setUserRoles(systemUser);
        return AuthenticationUtil.createFogbowToken(systemUser, this.privateKey, publicKeyString);
    }
}
