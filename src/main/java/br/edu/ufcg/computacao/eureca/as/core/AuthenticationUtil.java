package br.edu.ufcg.computacao.eureca.as.core;

import br.edu.ufcg.computacao.eureca.as.api.http.request.Token;
import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.constants.Messages;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthenticatedUserException;
import br.edu.ufcg.computacao.eureca.common.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthenticationUtil {
    private static final long EXPIRATION_INTERVAL = TimeUnit.DAYS.toMillis(1); // One day

    private static final Logger LOGGER = Logger.getLogger(AuthenticationUtil.class);

    public static SystemUser authenticate(PublicKey asPublicKey, String encryptedTokenValue)
            throws UnauthenticatedUserException {
        RSAPrivateKey privateKey = null;
        try {
            privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
            LOGGER.debug(String.format(Messages.ENCRYPTED_TOKEN_S_S, encryptedTokenValue, privateKey));
            String plainTokenValue = TokenProtector.decrypt(privateKey, encryptedTokenValue,
                    SystemConstants.TOKEN_STRING_SEPARATOR);
            String[] tokenFields = StringUtils.splitByWholeSeparator(plainTokenValue, SystemConstants.TOKEN_SEPARATOR);
            String payload = tokenFields[0];
            String signature = tokenFields[1];
            checkIfSignatureIsValid(asPublicKey, payload, signature);
            String[] payloadFields = StringUtils.splitByWholeSeparator(payload, SystemConstants.PAYLOAD_SEPARATOR);
            String federationUserString = payloadFields[0];
            String expirationTime = payloadFields[1];
            checkIfTokenHasNotExprired(expirationTime);
            return SystemUser.deserialize(federationUserString);
        } catch (InternalServerErrorException e) {
            throw new UnauthenticatedUserException(e.getMessage());
        }
    }

    public static String createEurecaToken(SystemUser systemUser, RSAPrivateKey privateKey, String publicKeyString)
            throws InternalServerErrorException {
        String tokenAttributes = SystemUser.serialize(systemUser);
        String expirationTime = generateExpirationTime();
        String payload = tokenAttributes + SystemConstants.PAYLOAD_SEPARATOR + expirationTime;
        try {
            String signature = CryptoUtil.sign(privateKey, payload);
            String signedUnprotectedToken = payload + SystemConstants.TOKEN_SEPARATOR + signature;
            RSAPublicKey publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
            return TokenProtector.encrypt(publicKey, signedUnprotectedToken, SystemConstants.TOKEN_STRING_SEPARATOR);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new InternalServerErrorException();
        }
    }

    private static void checkIfSignatureIsValid(PublicKey publicKey, String payload, String signature)
            throws UnauthenticatedUserException {

        try {
            if (!CryptoUtil.verify(publicKey, payload, signature)) {
                throw new UnauthenticatedUserException(Messages.INVALID_TOKEN);
            }
        } catch (SignatureException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new UnauthenticatedUserException(e.getMessage());
        }
    }

    private static void checkIfTokenHasNotExprired(String expirationTime) throws UnauthenticatedUserException {
        Date currentDate = new Date(getNow());
        Date expirationDate = new Date(Long.parseLong(expirationTime));
        if (expirationDate.before(currentDate)) {
            throw new UnauthenticatedUserException(Messages.EXPIRED_TOKEN);
        }
    }

    private static String generateExpirationTime() {
        Date expirationDate = new Date(getNow() + EXPIRATION_INTERVAL);
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    private static long getNow() {
        return System.currentTimeMillis();
    }
}
