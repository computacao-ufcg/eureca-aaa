package br.edu.ufcg.computacao.eureca.as.util;

import br.edu.ufcg.computacao.eureca.as.constants.EurecaAsConstants;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.EurecaAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.UnauthenticatedUserAsException;
import br.edu.ufcg.computacao.eureca.as.core.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.as.core.util.HomeDir;
import br.edu.ufcg.computacao.eureca.as.core.util.TokenProtector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.security.*;
import org.junit.Assert;

public class TokenProtectorTest {

    private String tokenSeparator;

    @Before
    public void setup() throws EurecaAsException, GeneralSecurityException, IOException {
        this.tokenSeparator = EurecaAsConstants.TOKEN_SEPARATOR;
    }

    @Test
    public void testEcryptAndDecrypt() throws IOException, GeneralSecurityException, InternalServerErrorAsException,
            UnauthenticatedUserAsException {
        // set up
        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        String originalToken = "This is the token to be encrypted and decrypted";
        PublicKey localPubKey = CryptoUtil.getPublicKey(pubKeyPath);
        PrivateKey localPrivKey = CryptoUtil.getPrivateKey(privKeyPath);

        // exercise
        String encryptedToken = TokenProtector.encrypt(localPubKey, originalToken, tokenSeparator);
        String decryptedToken = TokenProtector.decrypt(localPrivKey, encryptedToken, tokenSeparator);

        // exercise
        Assert.assertNotEquals(originalToken, encryptedToken);
        Assert.assertEquals(originalToken, decryptedToken);
    }

    @Test
    public void testRewrap() throws GeneralSecurityException, InternalServerErrorAsException, IOException,
            UnauthenticatedUserAsException {
        // set up
        String keysPath = HomeDir.getPath();
        String pubKeyPath = keysPath + "public.key";
        String privKeyPath = keysPath + "private.key";

        PublicKey localPubKey = CryptoUtil.getPublicKey(pubKeyPath);
        PrivateKey localPrivKey = CryptoUtil.getPrivateKey(privKeyPath);

        String originalToken = "This is the token to be encrypted and decrypted";
        String protectedToken = TokenProtector.encrypt(localPubKey, originalToken, tokenSeparator);

        KeyPair forwardKeyPair = CryptoUtil.generateKeyPair();
        PublicKey forwardPubKey = forwardKeyPair.getPublic();
        PrivateKey forwardPrivKey = forwardKeyPair.getPrivate();

        // exercise
        String newProtectedToken = TokenProtector.rewrap(localPrivKey, forwardPubKey, protectedToken, tokenSeparator);
        String decryptedNewToken = TokenProtector.decrypt(forwardPrivKey, newProtectedToken, tokenSeparator);

        // verify
        Assert.assertNotEquals(protectedToken, newProtectedToken);
        Assert.assertEquals(originalToken, decryptedNewToken);
    }
}