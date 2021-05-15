package br.edu.ufcg.computacao.eureca.as.api.http.response;

import io.swagger.annotations.ApiModelProperty;

public class VersionResponse {
    @ApiModelProperty(example = "1.0.0-as-c803775-common-4e0d74e")
    private String version;

    public VersionResponse() {}

    public VersionResponse(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
