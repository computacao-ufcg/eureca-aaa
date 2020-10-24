package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class UnavailableProviderAsException extends EurecaAsException {
    private static final long serialVersionUID = 1L;

    public UnavailableProviderAsException() {
        super(Messages.UNAVAILABLE_PROVIDER);
    }

    public UnavailableProviderAsException(String message) {
        super(message);
    }
}
