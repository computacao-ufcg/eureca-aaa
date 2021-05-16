package br.edu.ufcg.computacao.eureca.as.constants;

import java.util.Locale;

public class Messages {
    public static final String AUTHENTICATION_ERROR = "Authentication error.";
    public static final String DECRYPTING_S_S = "Decrypting [%s] with key [%s].";
    public static final String ERROR_READING_PRIVATE_KEY_FILE = "Error reading private key file.";
    public static final String ERROR_READING_PUBLIC_KEY_FILE = "Error reading public key file.";
    public static final String GENERATED_TOKEN_S_S = "Raw token (%s), encrypted token (%s).";
    public static final String INVALID_ALGORITHM_OR_ENCODING = "Invalid LDAP encryption algorithm or encoding.";
    public static final String INVALID_CHAR_C_FOR_RANDOM_KEY_S_AT_INDEX_D = "Invalid char \"%c\" for random key: \"%s\" at index %d.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String INVALID_FORMAT_SECRET = "Invalid format for the secret.";
    public static final String MAXIMUM_SIZE_EXCEEDED = "The serialized object is larger than allowed.";
    public static final String MISSING_LDAP_ENDPOINT = "No LDAP endpoint in configuration file.";
    public static final String OPERATION_RETURNED_ERROR_S = "Operation returned error: %s.";
    public static final String RECEIVING_CREATE_TOKEN_D = "Create token request received with a credentials map of size %sd.";
    public static final String RECEIVING_GET_PUBLIC_KEY_REQUEST = "Get public key received.";
    public static final String RECEIVING_GET_VERSION = "Get version received.";
    public static final String RETURNING_DECRYPTED_TOKEN_S = "Returning token [%s].";
    public static final String SECRET_ALREADY_EXISTS = "Secret already exists.";
    public static final String SECRET_CREATED_BEFORE_AS_START_TIME = "Secret was created before the start time of the AS service.";
    public static final String SECRET_VALIDATED = "Valid secret received.";
    public static final String UNABLE_TO_FIND_CLASS_S = "Unable to find class %s.";
    public static final String UNABLE_TO_FIND_SYSTEM_USER_CLASS = "Unable to find system user class.";
    public static final String UNABLE_TO_LOAD_LDAP_INFO = "Unable to load account information from LDAP service.";
}
