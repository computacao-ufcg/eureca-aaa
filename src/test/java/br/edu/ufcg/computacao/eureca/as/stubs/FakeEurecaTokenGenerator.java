package br.edu.ufcg.computacao.eureca.as.stubs;

import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.TokenProtector;
import br.edu.ufcg.computacao.eureca.as.core.models.SystemUser;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.common.util.HomeDir;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FakeEurecaTokenGenerator {

    private String provider;
    private PrivateKey privateKey;

    public FakeEurecaTokenGenerator() throws IOException, GeneralSecurityException {
        this.provider = "fake-provider";

        String keysPath = HomeDir.getPath();
        String privKeyPath = keysPath + "private.key";

        this.privateKey = CryptoUtil.getPrivateKey(privKeyPath);
    }

    public String createToken(String publicKeyString, int duration) throws EurecaException {
        String tokenAttributes = this.createToken();
        String expirationTime = generateExpirationTime(duration);
        String payload = tokenAttributes + SystemConstants.PAYLOAD_SEPARATOR + expirationTime;
        String signature = null;
        RSAPublicKey publicKey;
        String signedUnprotectedToken;
        try {
            signature = CryptoUtil.sign(this.privateKey, payload);
            signedUnprotectedToken = payload + SystemConstants.TOKEN_SEPARATOR + signature;
            publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new InternalServerErrorException();
        }
        return TokenProtector.encrypt(publicKey, signedUnprotectedToken, SystemConstants.TOKEN_STRING_SEPARATOR);
    }

    private String generateExpirationTime(int duration) {
        Date expirationDate = new Date(getNow() + TimeUnit.DAYS.toMillis(duration));
        String expirationTime = Long.toString(expirationDate.getTime());
        return expirationTime;
    }

    public long getNow() {
        return System.currentTimeMillis();
    }

    public String createToken() throws InternalServerErrorException {
        String userId = "fake-userid";
        String userName = "fake-username";

        SystemUser user = new SystemUser(userId, userName, this.provider);
        return SystemUser.serialize(user);
    }
}
