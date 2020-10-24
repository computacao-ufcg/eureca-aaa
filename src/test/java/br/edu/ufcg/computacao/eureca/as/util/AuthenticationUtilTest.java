package br.edu.ufcg.computacao.eureca.as.util;

import br.edu.ufcg.computacao.eureca.as.core.exceptions.EurecaAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.UnauthenticatedUserAsException;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.as.core.util.AuthenticationUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.ServiceAsymmetricKeysHolder;
import br.edu.ufcg.computacao.eureca.as.stubs.FakeEurecaTokenGenerator;

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
        ConfigureRSAKeyTest.init();

        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(ConfigureRSAKeyTest.publicKeyPath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(ConfigureRSAKeyTest.privateKeyPath);
    }

    @Before
    public void before() throws IOException, GeneralSecurityException {
        this.publicKey = CryptoUtil.getPublicKey(ConfigureRSAKeyTest.publicKeyPath);
        this.privateKey = CryptoUtil.getPrivateKey(ConfigureRSAKeyTest.privateKeyPath);

        this.publicKeyString = CryptoUtil.getKey(ConfigureRSAKeyTest.publicKeyPath);
        this.tokenGenerator = new FakeEurecaTokenGenerator();
    }

    @AfterClass
    public static void tearDown(){
        ConfigureRSAKeyTest.tearDown();
    }

    @Test
    public void testAuthenticationSuccessful() throws EurecaAsException {
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

    @Test(expected = UnauthenticatedUserAsException.class)
    public void testExpiredToken() throws EurecaAsException {
        // set up
        String token = tokenGenerator.createToken(publicKeyString, 0);

        // exercise/verify
        SystemUser systemUser = AuthenticationUtil.authenticate(publicKey, token);
    }

    @Test(expected = UnauthenticatedUserAsException.class)
    public void testInvalidSignature() throws EurecaAsException, GeneralSecurityException {
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