package br.edu.ufcg.computacao.eureca.as.api.http;

import br.edu.ufcg.computacao.eureca.common.exceptions.CommonExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AsExceptionToHttpErrorConditionTranslator extends CommonExceptionHandler {
}
