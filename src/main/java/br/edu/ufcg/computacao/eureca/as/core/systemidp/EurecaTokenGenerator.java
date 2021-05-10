package br.edu.ufcg.computacao.eureca.as.core.systemidp;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.core.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class EurecaTokenGenerator {
    private static final Logger LOGGER = Logger.getLogger(EurecaTokenGenerator.class);

    private SystemIdentityProviderPlugin embeddedPlugin;

    private RSAPrivateKey privateKey;

    private SystemRolePlugin systemRoleProvider;
    
    public EurecaTokenGenerator(SystemIdentityProviderPlugin embeddedPlugin, SystemRolePlugin systemRoleProvider) {
        this.embeddedPlugin = embeddedPlugin;
        try {
            this.privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (InternalServerErrorException e) {
            throw new FatalErrorException(e.getMessage(), e);
        }
        this.systemRoleProvider = systemRoleProvider;
    }

    public String createToken(Map<String, String> userCredentials, String publicKeyString) throws EurecaException {
        SystemUser systemUser = this.embeddedPlugin.getSystemUser(userCredentials);
        systemRoleProvider.setUserRoles(systemUser);
        return AuthenticationUtil.createEurecaToken(systemUser, this.privateKey, publicKeyString);
    }
}
