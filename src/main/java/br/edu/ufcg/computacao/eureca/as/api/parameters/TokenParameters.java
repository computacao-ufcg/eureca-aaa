package br.edu.ufcg.computacao.eureca.as.api.parameters;

import br.edu.ufcg.computacao.eureca.as.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel
public class TokenParameters {
    //    @ApiModelProperty(position = 0, required = true, example = ApiDocumentation.Model.CREDENTIALS)
//    private Map<String, String> credentials;
    @ApiModelProperty(required = true)
    private String username;
    @ApiModelProperty(position = 1, required = true)
    private String password;
    @ApiModelProperty(position = 2, required = true, example = ApiDocumentation.Model.PUBLIC_KEY)
    private String publicKey;

    public Map<String, String> getCredentials() {
        return Map.of("username", username, "password", password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
