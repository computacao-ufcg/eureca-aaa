package br.edu.ufcg.computacao.eureca.as.util;

import br.edu.ufcg.computacao.eureca.as.core.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.as.stubs.FakeEurecaTokenGenerator;

import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthenticatedUserException;
import br.edu.ufcg.computacao.eureca.common.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.junit.*;
import java.io.IOException;
import java.security.*;

public class AuthenticationUtilTest {
    private FakeEurecaTokenGenerator tokenGenerator;

    private String publicKeyString;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @BeforeClass
    public static void setUp() throws Exception {
        String keysPath = HomeDir.getPath();
        String publicKeyPath = keysPath + "public.key";
        String privateKeyPath = keysPath + "private.key";

        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(publicKeyPath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privateKeyPath);
    }

    @Before
    public void before() throws IOException, GeneralSecurityException {
        String keysPath = HomeDir.getPath();
        String publicKeyPath = keysPath + "public.key";
        String privateKeyPath = keysPath + "private.key";

        this.publicKey = CryptoUtil.getPublicKey(publicKeyPath);
        this.privateKey = CryptoUtil.getPrivateKey(privateKeyPath);

        this.publicKeyString = CryptoUtil.getKey(publicKeyPath);
        this.tokenGenerator = new FakeEurecaTokenGenerator();
    }

    @Test
    public void testAuthenticationSuccessful() throws EurecaException {
        // set up
        String token = tokenGenerator.createToken(publicKeyString, 1);

        // exercise
        SystemUser systemUser = null;
        try {
            systemUser = AuthenticationUtil.authenticate(publicKey, token);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        // verify
        Assert.assertNotEquals(null, systemUser);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void testExpiredToken() throws EurecaException {
        // set up
        String token = tokenGenerator.createToken(publicKeyString, 0);

        // exercise/verify
        SystemUser systemUser = AuthenticationUtil.authenticate(publicKey, token);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void testInvalidSignature() throws EurecaException, GeneralSecurityException {
        // set up
        KeyPair keyPair = CryptoUtil.generateKeyPair();
        PublicKey differentKey = keyPair.getPublic();
        String differentKeyString = CryptoUtil.toBase64(differentKey);
        String token = tokenGenerator.createToken(differentKeyString, 1);

        // exercise
        // Try to verify the signature of a different key
        SystemUser systemUser = AuthenticationUtil.authenticate(publicKey, token);
    }
}