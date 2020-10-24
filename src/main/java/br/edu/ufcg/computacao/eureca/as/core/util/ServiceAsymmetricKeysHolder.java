package br.edu.ufcg.computacao.eureca.as.core.util;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class ServiceAsymmetricKeysHolder {
	private static final Logger LOGGER = Logger.getLogger(ServiceAsymmetricKeysHolder.class);

	private RSAPublicKey servicePublicKey;
	private RSAPrivateKey servicePrivateKey;
	private String publicKeyFilePath;
	private String privateKeyFilePath;
	private static ServiceAsymmetricKeysHolder instance;
	
    public static synchronized ServiceAsymmetricKeysHolder getInstance() {
        if (instance == null) {
            instance = new ServiceAsymmetricKeysHolder();
        }
        return instance;
    }

    public void setPublicKeyFilePath(String publicKeyFilePath) {
        this.publicKeyFilePath = publicKeyFilePath;
    }

    public void setPrivateKeyFilePath(String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }

    public RSAPublicKey getPublicKey() throws InternalServerErrorAsException {
	    if (this.servicePublicKey == null) {
	        if (this.publicKeyFilePath == null) throw new InternalServerErrorAsException(Messages.NO_PUBLIC_KEY_DEFINED);
            try {
                this.servicePublicKey = CryptoUtil.getPublicKey(this.publicKeyFilePath);
            } catch (IOException | GeneralSecurityException e) {
                throw new InternalServerErrorAsException(Messages.INVALID_PUBLIC_KEY);
            }
        }
	    return this.servicePublicKey;
    }

    public RSAPrivateKey getPrivateKey() throws InternalServerErrorAsException {
        if (this.servicePrivateKey == null) {
            if (this.privateKeyFilePath == null) throw new InternalServerErrorAsException(Messages.NO_PRIVATE_KEY_DEFINED);
            try {
                this.servicePrivateKey = CryptoUtil.getPrivateKey(this.privateKeyFilePath);
            } catch (IOException | GeneralSecurityException e) {
                throw new InternalServerErrorAsException(Messages.INVALID_PRIVATE_KEY);
            }
        }
        return this.servicePrivateKey;
    }
}
