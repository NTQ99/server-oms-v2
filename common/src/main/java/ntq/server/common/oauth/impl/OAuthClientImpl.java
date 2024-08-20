package ntq.server.common.oauth.impl;

import com.dslplatform.json.JsonReader;
import ntq.server.common.exception.OAuthException;
import ntq.server.common.oauth.AccessToken;
import ntq.server.common.oauth.OAuthClient;
import ntq.server.common.util.CompletableFutures;
import ntq.server.common.util.Json;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class OAuthClientImpl implements OAuthClient {
  private static final JsonReader.ReadObject<AccessToken> objectReader = Json.findReader(AccessToken.class);
  private final HttpClient httpClient;
  private final HttpRequest request;

  OAuthClientImpl(HttpClient httpClient, URI tokenApiUrl, byte[] formData) {
    this.httpClient = httpClient;
    request = HttpRequest.newBuilder(tokenApiUrl)
        .header("content-type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofByteArray(formData))
        .build();
  }

  @Override
  public CompletableFuture<AccessToken> getAccessToken() {
    return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray()).thenApply(response -> {
      if (response.statusCode() == 200) {
        var token = Json.decode(response.body(), objectReader);
        if (token.getExpiresAt() == 0) {
          token.setExpiresAt(System.currentTimeMillis() + token.getExpiresIn() * 1000 - 60_000L);
        }
        return token;
      }
      throw CompletableFutures.toCompletionException(new OAuthException(new String(response.body(), StandardCharsets.UTF_8)));
    });
  }
}