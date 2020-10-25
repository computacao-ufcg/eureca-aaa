package br.edu.ufcg.computacao.eureca.as.core.systemidp;

import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;

import java.util.Map;

public interface SystemIdentityProviderPlugin<T extends SystemUser> {
    /**
     * Authenticates the user using the provided credentials and in case of success returns a representation of
     * the authenticated user.
     *
     * @param userCredentials a map containing the credentials to authenticate the user with the identity provider
     *                        service.
     * @return a SystemUser object that represents the successfully authenticated user and uniquely identifies
     * the user in the whole system.
     */
    public T getSystemUser(Map<String, String> userCredentials) throws EurecaException;
}
