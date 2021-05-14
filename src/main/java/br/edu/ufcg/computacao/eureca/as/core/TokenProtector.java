package br.edu.ufcg.computacao.eureca.as.core;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthenticatedUserException;
import br.edu.ufcg.computacao.eureca.common.util.CryptoUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.security.Key;

public class TokenProtector {
    private static final Logger LOGGER = Logger.getLogger(TokenProtector.class);

    // Encrypts a token string so that only the possessor of a private key corresponding to the
    // public key given as parameter is able to read the token string.
    public static String encrypt(Key key, String unprotectedToken, String tokenSeparator) throws InternalServerErrorException {
        String randomKey;
        String encryptedToken;
        String encryptedKey;
        try {
            randomKey = CryptoUtil.generateAESKey();
            encryptedToken = CryptoUtil.encryptAES(randomKey.getBytes("UTF-8"), unprotectedToken);
            encryptedKey = CryptoUtil.encrypt(randomKey, key);
            return encryptedKey + tokenSeparator + encryptedToken;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    // Decrypts the token string using the provided key.
    public static String decrypt(Key key, String protectedToken, String tokenSeparator)
            throws UnauthenticatedUserException {
        LOGGER.debug("DECRYPT!!!");
        String randomKey;
        String decryptedToken;
        String[] tokenParts = StringUtils.splitByWholeSeparator(protectedToken, tokenSeparator);

        try {
            LOGGER.debug(String.format(Messages.DECRYPTING_S_S, tokenParts[0], key.toString()));
            randomKey = CryptoUtil.decrypt(tokenParts[0], key);
            LOGGER.debug(String.format(Messages.DECRYPTING_S_S, tokenParts[1], randomKey));
            decryptedToken = CryptoUtil.decryptAES(randomKey.getBytes("UTF-8"), tokenParts[1]);
            LOGGER.debug(String.format(Messages.RETURNING_DECRYPTED_TOKEN_S, decryptedToken));
            return decryptedToken;
        } catch (Exception e) {
            throw new UnauthenticatedUserException();
        }
    }

    // Decrypts the token and re-encrypts it with a new publicKey; this is needed before forwarding the token
    // to another service.
    public static String rewrap(Key decryptKey, Key encryptKey, String protectedToken, String tokenSeparator)
            throws UnauthenticatedUserException, InternalServerErrorException {
        String unprotectedToken = decrypt(decryptKey, protectedToken, tokenSeparator);
        String newProtectedToken = encrypt(encryptKey, unprotectedToken, tokenSeparator);
        return newProtectedToken;
    }
}
