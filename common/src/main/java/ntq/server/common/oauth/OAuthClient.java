package ntq.server.common.oauth;

import ntq.server.common.oauth.impl.OAuthClientBuilder;

import java.util.concurrent.CompletableFuture;

public interface OAuthClient {
  CompletableFuture<AccessToken> getAccessToken();

  static OAuthClientBuilder builder() {
    return new OAuthClientBuilder();
  }
}