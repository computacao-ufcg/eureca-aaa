package br.edu.ufcg.computacao.eureca.as.api.http.request;

import br.edu.ufcg.computacao.eureca.as.api.http.response.TokenResponse;
import br.edu.ufcg.computacao.eureca.as.api.parameters.TokenParameters;
import br.edu.ufcg.computacao.eureca.as.constants.ApiDocumentation;
import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.ApplicationFacade;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = Token.ENDPOINT)
@Api(description = ApiDocumentation.Token.API)
public class Token {
    public static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "tokens";

    private final Logger LOGGER = Logger.getLogger(Token.class);

    @ApiOperation(value = ApiDocumentation.Token.CREATE_OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> createTokenValue(
            @ApiParam(value = ApiDocumentation.Token.CREATE_REQUEST_BODY)
            @RequestBody TokenParameters request)
            throws EurecaException {

        try {
            LOGGER.info(String.format(Messages.RECEIVING_CREATE_TOKEN_D, request.getCredentials().size()));
            String tokenValue = ApplicationFacade.getInstance().createToken(
                    request.getCredentials(), request.getPublicKey());
            TokenResponse token = new TokenResponse(tokenValue);
            LOGGER.debug(String.format(Messages.GENERATED_TOKEN_S_S, tokenValue, token.getToken()));
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.OPERATION_RETURNED_ERROR_S, e.getMessage()), e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
