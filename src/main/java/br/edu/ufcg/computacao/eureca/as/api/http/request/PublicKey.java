package br.edu.ufcg.computacao.eureca.as.api.http.request;

import br.edu.ufcg.computacao.eureca.as.constants.ApiDocumentation;
import br.edu.ufcg.computacao.eureca.as.constants.Messages;
import br.edu.ufcg.computacao.eureca.as.constants.SystemConstants;
import br.edu.ufcg.computacao.eureca.as.core.ApplicationFacade;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.EurecaAsException;
import br.edu.ufcg.computacao.eureca.as.core.exceptions.InternalServerErrorAsException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = PublicKey.PUBLIC_KEY_ENDPOINT)
@Api(description = ApiDocumentation.PublicKey.API)
public class PublicKey {
    public static final String PUBLIC_KEY_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "publicKey";

    private final Logger LOGGER = Logger.getLogger(PublicKey.class);

    @ApiOperation(value = ApiDocumentation.PublicKey.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<br.edu.ufcg.computacao.eureca.as.api.http.response.PublicKey> getPublicKey()
            throws EurecaAsException {
        try {
            LOGGER.info(Messages.RECEIVING_GET_PUBLIC_KEY_REQUEST);
            String publicKeyValue = ApplicationFacade.getInstance().getPublicKey();
            br.edu.ufcg.computacao.eureca.as.api.http.response.PublicKey publicKey =
                    new br.edu.ufcg.computacao.eureca.as.api.http.response.PublicKey(publicKeyValue);
            return new ResponseEntity<>(publicKey, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.OPERATION_RETURNED_ERROR_S, e.getMessage()), e);
            throw new InternalServerErrorAsException(e.getMessage());
        }
    }
}
