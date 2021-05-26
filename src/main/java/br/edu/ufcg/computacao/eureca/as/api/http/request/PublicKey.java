package br.edu.ufcg.computacao.eureca.as.api.http.request;

import br.edu.ufcg.computacao.eureca.as.api.http.response.PublicKeyResponse;
import br.edu.ufcg.computacao.eureca.as.constants.ApiDocumentation;
import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.ApplicationFacade;
import br.edu.ufcg.computacao.eureca.common.exceptions.EurecaException;
import br.edu.ufcg.computacao.eureca.common.exceptions.InternalServerErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = PublicKey.ENDPOINT)
@Api(description = ApiDocumentation.PublicKey.API)
public class PublicKey {
    public static final String ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "publicKey";

    private final Logger LOGGER = Logger.getLogger(PublicKey.class);

    @ApiOperation(value = ApiDocumentation.PublicKey.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PublicKeyResponse> getPublicKey()
            throws EurecaException {
        try {
            LOGGER.info(br.edu.ufcg.computacao.eureca.common.constants.Messages.RECEIVING_GET_PUBLIC_KEY_REQUEST);
            String publicKeyValue = ApplicationFacade.getInstance().getPublicKey();
            PublicKeyResponse publicKey = new PublicKeyResponse(publicKeyValue);
            return new ResponseEntity<>(publicKey, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.OPERATION_RETURNED_ERROR_S, e.getMessage()), e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
