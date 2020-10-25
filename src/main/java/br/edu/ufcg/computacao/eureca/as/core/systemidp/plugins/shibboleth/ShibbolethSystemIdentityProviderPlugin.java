package br.edu.ufcg.computacao.eureca.as.core.systemidp.plugins.shibboleth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.core.PropertiesHolder;
import br.edu.ufcg.computacao.eureca.as.core.models.ShibbolethSystemUser;
import br.edu.ufcg.computacao.eureca.as.core.systemidp.SystemIdentityProviderPlugin;
import br.edu.ufcg.computacao.eureca.common.exceptions.FatalErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import br.edu.ufcg.computacao.eureca.common.exceptions.UnauthenticatedUserException;
import br.edu.ufcg.computacao.eureca.common.util.CryptoUtil;
import br.edu.ufcg.computacao.eureca.common.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;

public class ShibbolethSystemIdentityProviderPlugin implements SystemIdentityProviderPlugin<ShibbolethSystemUser> {
	private static final Logger LOGGER = Logger.getLogger(ShibbolethSystemIdentityProviderPlugin.class);

	// Shib token parameters
	private static final int SHIB_TOKEN_PARAMETERS_SIZE = 5;
	private static final int SAML_ATTRIBUTES_ATTR_SHIB_INDEX = 4;
	private static final int COMMON_NAME_ATTR_SHIB_INDEX = 3;
	private static final int EDU_PRINCIPAL_NAME_ATTR_SHIB_INDEX = 2;
	private static final int ASSERTION_URL_ATTR_SHIB_INDEX = 1;
	private static final int SECREC_ATTR_SHIB_INDEX = 0;

	// credentials
	private static final String TOKEN_CREDENTIAL = "token";
	private static final String KEY_SIGNATURE_CREDENTIAL = "keySignature";
	private static final String KEY_CREDENTIAL = "key";

	// Shib-specific token constants
	public static final String SHIB_TOKEN_STRING_SEPARATOR = "!#!";

	private String identityProviderId;
	private RSAPrivateKey asPrivateKey;
	private RSAPublicKey shibAppPublicKey;
	private SecretManager secretManager;

	public ShibbolethSystemIdentityProviderPlugin() {
		this.identityProviderId = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.PROVIDER_ID_KEY);
        try {
			this.asPrivateKey = ServiceAsymmetricKeysHolder.getInstance().getPrivateKey();
        } catch (InternalServerErrorException e) {
            throw new FatalErrorException(
            		String.format(Messages.ERROR_READING_PRIVATE_KEY_FILE, e.getMessage()));
        }
		try {
			this.shibAppPublicKey = getShibbolethApplicationPublicKey();
		} catch (IOException | GeneralSecurityException e) {
			throw new FatalErrorException(
					String.format(Messages.ERROR_READING_PUBLIC_KEY_FILE, e.getMessage()));
		}
		this.secretManager = new SecretManager();
	}
	
	@Override
	public ShibbolethSystemUser getSystemUser(Map<String, String> userCredentials) throws UnauthenticatedUserException {
		String tokenShibAppEncrypted = userCredentials.get(TOKEN_CREDENTIAL);
		String keyShibAppEncrypted = userCredentials.get(KEY_CREDENTIAL);
		String keySignatureShibApp = userCredentials.get(KEY_SIGNATURE_CREDENTIAL);
		
		String keyShibApp = decryptKeyShib(keyShibAppEncrypted);
		String tokenShib = decryptTokenShib(keyShibApp, tokenShibAppEncrypted);
		verifyShibAppKeyAuthenticity(keySignatureShibApp, keyShibApp);

		String[] tokenShibAppParameters = tokenShib.split(SHIB_TOKEN_STRING_SEPARATOR);
		checkTokenFormat(tokenShibAppParameters);
		
		verifySecretShibAppToken(tokenShibAppParameters);
		
		return createShibbolethSystemUser(tokenShibAppParameters);
	}

	protected void verifySecretShibAppToken(String[] tokenShibParameters) throws UnauthenticatedUserException {
		String secret = tokenShibParameters[SECREC_ATTR_SHIB_INDEX];
		boolean isValid = this.secretManager.verify(secret);
		if (!isValid) {
        	LOGGER.error(Messages.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
		}
	}

	protected void checkTokenFormat(String[] tokenShibParameters) throws UnauthenticatedUserException {
		if (tokenShibParameters.length != SHIB_TOKEN_PARAMETERS_SIZE) {
        	LOGGER.error(Messages.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException();
		}
	}

	protected ShibbolethSystemUser createShibbolethSystemUser(String[] tokenShibParameters) {
		String assertionUrl = tokenShibParameters[ASSERTION_URL_ATTR_SHIB_INDEX];
		String eduPrincipalName = tokenShibParameters[EDU_PRINCIPAL_NAME_ATTR_SHIB_INDEX];
		String commonName = tokenShibParameters[COMMON_NAME_ATTR_SHIB_INDEX];
		// attributes in json format, like this "{\"key\": \"value\"}"
		String samlAttributes = tokenShibParameters[SAML_ATTRIBUTES_ATTR_SHIB_INDEX];
		return new ShibbolethSystemUser(eduPrincipalName, commonName, this.identityProviderId, assertionUrl, samlAttributes);
	}

	protected void verifyShibAppKeyAuthenticity(String signature, String message) throws UnauthenticatedUserException {
		try {
			CryptoUtil.verify(this.shibAppPublicKey, message, signature);
		} catch (Exception e) {
        	LOGGER.error(Messages.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException(e.getMessage());
		}
	}

	protected String decryptTokenShib(String keyShib, String rasToken) throws UnauthenticatedUserException {
		String tokenShibApp = null;
		try {
			tokenShibApp = CryptoUtil.decryptAES(keyShib.getBytes(CryptoUtil.UTF_8), rasToken);
		} catch (Exception e) {
        	LOGGER.error(Messages.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException(e.getMessage());
		}
		return tokenShibApp;
	}
	
	protected String decryptKeyShib(String keyShibAppEncrypted) throws UnauthenticatedUserException {
		String keyShibApp = null;
		try {
			keyShibApp = CryptoUtil.decrypt(keyShibAppEncrypted, this.asPrivateKey);
		} catch (Exception e) {
        	LOGGER.error(Messages.AUTHENTICATION_ERROR);
            throw new UnauthenticatedUserException(e.getMessage());
		}
		return keyShibApp;
	}
	
    protected RSAPublicKey getShibbolethApplicationPublicKey() throws IOException, GeneralSecurityException {
        String shibPublicKeyPath = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.SHIB_PUBLIC_FILE_PATH_KEY);
        return CryptoUtil.getPublicKey(shibPublicKeyPath);
    }
}
