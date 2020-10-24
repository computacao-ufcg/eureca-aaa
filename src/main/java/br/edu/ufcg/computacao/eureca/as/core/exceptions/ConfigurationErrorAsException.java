package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class ConfigurationErrorAsException extends EurecaAsException {
    private static final long serialVersionUID = 1L;

    public ConfigurationErrorAsException() {
        super(Messages.CONFIGURATION_ERROR);
    }

    public ConfigurationErrorAsException(String message) {
        super(message);
    }
}

