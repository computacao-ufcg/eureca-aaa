package br.edu.ufcg.computacao.eureca.as.core.util;

import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.UnauthenticatedUserAsException;
import org.apache.commons.lang.StringUtils;
import java.security.Key;

public class TokenProtector {
    // Encrypts a token string so that only the possessor of a private key corresponding to the
    // public key given as parameter is able to read the token string.
    public static String encrypt(Key key, String unprotectedToken, String tokenSeparator) throws InternalServerErrorAsException {
        String randomKey;
        String encryptedToken;
        String encryptedKey;
        try {
            randomKey = CryptoUtil.generateAESKey();
            encryptedToken = CryptoUtil.encryptAES(randomKey.getBytes("UTF-8"), unprotectedToken);
            encryptedKey = CryptoUtil.encrypt(randomKey, key);
            return encryptedKey + tokenSeparator + encryptedToken;
        } catch (Exception e) {
            throw new InternalServerErrorAsException();
        }
    }

    // Decrypts the token string using the provided key.
    public static String decrypt(Key key, String protectedToken, String tokenSeparator)
            throws UnauthenticatedUserAsException {
        String randomKey;
        String decryptedToken;
        String[] tokenParts = StringUtils.splitByWholeSeparator(protectedToken, tokenSeparator);

        try {
            randomKey = CryptoUtil.decrypt(tokenParts[0], key);
            decryptedToken = CryptoUtil.decryptAES(randomKey.getBytes("UTF-8"), tokenParts[1]);
            return decryptedToken;
        } catch (Exception e) {
            throw new UnauthenticatedUserAsException();
        }
    }

    // Decrypts the token and re-encrypts it with a new publicKey; this is needed before forwarding the token
    // to another service.
    public static String rewrap(Key decryptKey, Key encryptKey, String protectedToken, String tokenSeparator)
            throws UnauthenticatedUserAsException, InternalServerErrorAsException {
        String unprotectedToken = decrypt(decryptKey, protectedToken, tokenSeparator);
        String newProtectedToken = encrypt(encryptKey, unprotectedToken, tokenSeparator);
        return newProtectedToken;
    }
}
