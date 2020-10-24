package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class InvalidParameterAsException extends EurecaAsException {
    private static final long serialVersionUID = 1L;

    public InvalidParameterAsException() {
        super(Messages.UNEXPECTED);
    }

    public InvalidParameterAsException(String message) {
        super(message);
    }
}
