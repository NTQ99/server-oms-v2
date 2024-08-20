package ntq.server.common.oauth;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@CompiledJson
public class AccessToken {
  String token;
  String tokenType;
  long expiresAt;
  long expiresIn;

  public boolean isExpired() {
    return System.currentTimeMillis() > expiresAt;
  }

  @JsonProperty("access_token")
  public String getToken() {
    return token;
  }

  @JsonProperty("token_type")
  public String getTokenType() {
    return tokenType;
  }

  @JsonProperty("expires_at")
  public long getExpiresAt() {
    return expiresAt;
  }

  @JsonProperty("expires_in")
  public long getExpiresIn() {
    return expiresIn;
  }
}