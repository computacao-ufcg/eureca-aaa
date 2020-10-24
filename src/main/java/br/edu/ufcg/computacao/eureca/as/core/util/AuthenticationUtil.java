package br.edu.ufcg.computacao.eureca.as.core.util;

import br.edu.ufcg.computacao.eureca.as.constants.EurecaAsConstants;
import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.UnauthenticatedUserAsException;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import org.apache.commons.lang.StringUtils;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AuthenticationUtil {
    private static final long EXPIRATION_INTERVAL = TimeUnit.DAYS.toMillis(1); // One day

    public static SystemUser authenticate(PublicKey asPublicKey, String encryptedTokenValue)
            throws UnauthenticatedUserAsException {
        RSAPrivateKey privateKey = null;
        try {
            privateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
            String plainTokenValue = TokenProtector.decrypt(privateKey, encryptedTokenValue,
                    EurecaAsConstants.TOKEN_STRING_SEPARATOR);
            String[] tokenFields = StringUtils.splitByWholeSeparator(plainTokenValue, EurecaAsConstants.TOKEN_SEPARATOR);
            String payload = tokenFields[0];
            String signature = tokenFields[1];
            checkIfSignatureIsValid(asPublicKey, payload, signature);
            String[] payloadFields = StringUtils.splitByWholeSeparator(payload, EurecaAsConstants.PAYLOAD_SEPARATOR);
            String federationUserString = payloadFields[0];
            String expirationTime = payloadFields[1];
            checkIfTokenHasNotExprired(expirationTime);
            return SystemUser.deserialize(federationUserString);
        } catch (InternalServerErrorAsException e) {
            throw new UnauthenticatedUserAsException(e.getMessage());
        }
    }

    public static String createEurecaToken(SystemUser systemUser, RSAPrivateKey privateKey, String publicKeyString)
            throws InternalServerErrorAsException {
        String tokenAttributes = SystemUser.serialize(systemUser);
        String expirationTime = generateExpirationTime();
        String payload = tokenAttributes + EurecaAsConstants.PAYLOAD_SEPARATOR + expirationTime;
        try {
            String signature = CryptoUtil.sign(privateKey, payload);
            String signedUnprotectedToken = payload + EurecaAsConstants.TOKEN_SEPARATOR + signature;
            RSAPublicKey publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
            return TokenProtector.encrypt(publicKey, signedUnprotectedToken, EurecaAsConstants.TOKEN_STRING_SEPARATOR);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new InternalServerErrorAsException();
        }
    }

    private static void checkIfSignatureIsValid(PublicKey publicKey, String payload, String signature)
            throws UnauthenticatedUserAsException {

        try {
            if (!CryptoUtil.verify(publicKey, payload, signature)) {
                throw new UnauthenticatedUserAsException(Messages.INVALID_TOKEN);
            }
        } catch (SignatureException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new UnauthenticatedUserAsException(e.getMessage());
        }
    }

    private static void checkIfTokenHasNotExprired(String expirationTime) throws UnauthenticatedUserAsException {
        Date currentDate = new Date(getNow());
        Date expirationDate = new Date(Long.parseLong(expirationTime));
        if (expirationDate.before(currentDate)) {
            throw new UnauthenticatedUserAsException(Messages.EXPIRED_TOKEN);
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
