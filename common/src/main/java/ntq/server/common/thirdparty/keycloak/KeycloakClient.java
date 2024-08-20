package ntq.server.common.thirdparty.keycloak;

import ntq.server.common.jws.JWKSet;

import java.util.concurrent.CompletableFuture;

public interface KeycloakClient {
  CompletableFuture<JWKSet> getJWKSet();
}
