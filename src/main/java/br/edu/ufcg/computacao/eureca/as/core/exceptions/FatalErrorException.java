package br.edu.ufcg.computacao.eureca.as.core.exceptions;

import br.edu.ufcg.computacao.eureca.as.constants.Messages;

public class FatalErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FatalErrorException() {
        super(Messages.FATAL_ERROR);
    }

    public FatalErrorException(Throwable cause) {
        super(Messages.FATAL_ERROR, cause);
    }

    public FatalErrorException(String message) {
        super(message);
    }

    public FatalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
