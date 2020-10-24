package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class InternalServerErrorAsException extends EurecaAsException {
    private static final long serialVersionUID = 1L;

    public InternalServerErrorAsException() {
        super(Messages.UNEXPECTED);
    }

    public InternalServerErrorAsException(String message) {
        super(message);
    }
}
