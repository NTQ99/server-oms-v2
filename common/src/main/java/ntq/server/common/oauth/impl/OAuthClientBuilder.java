package ntq.server.common.oauth.impl;

import lombok.Setter;
import lombok.experimental.Accessors;
import ntq.server.common.oauth.OAuthClient;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

@Setter
@Accessors(chain = true)
public class OAuthClientBuilder {
  private HttpClient httpClient;
  private URI tokenApiUrl;
  private String grantType;
  private String clientId;
  private String clientSecret;
  private String scope;
  private String username;
  private String password;
  private boolean useCache;

  public OAuthClient build() {
    var formData = new StringBuilder();
    buildFormData(formData, "grant_type", grantType);
    buildFormData(formData, "client_id", clientId);
    buildFormData(formData, "client_secret", clientSecret);
    buildFormData(formData, "scope", scope);
    buildFormData(formData, "username", username);
    buildFormData(formData, "password", password);
    if (formData.length() > 0) {
      formData.deleteCharAt(formData.length() - 1);
    }
    var client = new OAuthClientImpl(httpClient, tokenApiUrl, formData.toString().getBytes(StandardCharsets.UTF_8));
    return useCache ? new CachedOAuthClient(client) : client;
  }

  private void buildFormData(StringBuilder builder, String fieldName, String value) {
    if (value != null) {
      builder.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8)).append('&');
    }
  }
}