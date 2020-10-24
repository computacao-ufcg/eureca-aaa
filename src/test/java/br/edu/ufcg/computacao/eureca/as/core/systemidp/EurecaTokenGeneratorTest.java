package br.edu.ufcg.computacao.eureca.as.core.systemidp;

import br.edu.ufcg.computacao.eureca.as.core.exceptions.EurecaAsException;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.util.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.ServiceAsymmetricKeysHolder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

@PrepareForTest({AuthenticationUtil.class, ServiceAsymmetricKeysHolder.class})
@RunWith(PowerMockRunner.class)
public class EurecaTokenGeneratorTest {

    public static final String VALID_TOKEN = "token";
    public static final String VALID_PUBLIC_KEY = "validPublicKey";

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String IDENTITY_PROVIDER_ID = "identityProviderId";

    @Test
    public void testCreateToken() throws EurecaAsException {
        PowerMockito.mockStatic(ServiceAsymmetricKeysHolder.class);
        BDDMockito.given(ServiceAsymmetricKeysHolder.getInstance()).willReturn(Mockito.mock(ServiceAsymmetricKeysHolder.class));

        PowerMockito.mockStatic(AuthenticationUtil.class);
        BDDMockito.given(AuthenticationUtil.createEurecaToken(Mockito.any(SystemUser.class), Mockito.any(RSAPrivateKey.class), Mockito.anyString())).willReturn(VALID_TOKEN);

        SystemIdentityProviderPlugin identityProviderPlugin = Mockito.mock(SystemIdentityProviderPlugin.class);
        Mockito.when(identityProviderPlugin.getSystemUser(Mockito.anyMap())).thenReturn(new SystemUser(USER_ID, USER_NAME, IDENTITY_PROVIDER_ID));

        SystemRolePlugin roleProviderPlugin = Mockito.mock(SystemRolePlugin.class);
        
        EurecaTokenGenerator eurecaTokenGenerator = new EurecaTokenGenerator(identityProviderPlugin, roleProviderPlugin);
        String token = eurecaTokenGenerator.createToken(new HashMap<String, String>(), VALID_PUBLIC_KEY);

        Assert.assertEquals(VALID_TOKEN, token);
    }
}
