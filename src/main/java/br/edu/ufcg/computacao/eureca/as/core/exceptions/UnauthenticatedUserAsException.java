package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class UnauthenticatedUserAsException extends EurecaAsException {
    private static final long serialVersionUID = 1L;

    public UnauthenticatedUserAsException() {
        super(Messages.AUTHENTICATION_ERROR);
    }

    public UnauthenticatedUserAsException(String message) {
        super(message);
    }
}
