package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class UnauthorizedRequestAsException extends EurecaAsException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedRequestAsException() {
        super(Messages.AUTHORIZATION_ERROR);
    }

    public UnauthorizedRequestAsException(String message) {
        super(message);
    }
}
