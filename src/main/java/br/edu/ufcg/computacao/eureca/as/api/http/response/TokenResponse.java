package br.edu.ufcg.computacao.eureca.as.api.http.response;

import br.edu.ufcg.computacao.eureca.as.constants.ApiDocumentation;
import io.swagger.annotations.ApiModelProperty;

public class TokenResponse {
    @ApiModelProperty(example = ApiDocumentation.Model.TOKEN)
    private String token;

    public TokenResponse() {}

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
