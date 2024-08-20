package ntq.server.common.thirdparty.keycloak.impl;

import com.dslplatform.json.JsonReader;
import ntq.server.common.jws.JWKSet;
import ntq.server.common.thirdparty.keycloak.KeycloakClient;
import ntq.server.common.util.Json;
import ntq.server.common.util.RestClient;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class KeycloakClientImpl implements KeycloakClient {
  private static final JsonReader.ReadObject<JWKSet> JWK_SET_READ_OBJECT = Json.findReader(JWKSet.class);
  private final RestClient restClient;
  private final URI jwksUrl;

  public KeycloakClientImpl(RestClient restClient, String jwksUrl) {
    this.restClient = restClient;
    this.jwksUrl = URI.create(jwksUrl);
  }

  @Override
  public CompletableFuture<JWKSet> getJWKSet() {
    return restClient.getForObject(jwksUrl, JWK_SET_READ_OBJECT);
  }
}
