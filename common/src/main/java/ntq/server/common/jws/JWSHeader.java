package ntq.server.common.jws;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@CompiledJson
@Data
@Accessors(chain = true)
public class JWSHeader {
  @JsonProperty("alg")
  private String algorithm;
  @JsonProperty("typ")
  private String type;
  @JsonProperty("cty")
  private String contentType;
  @JsonProperty("kid")
  private String keyId;
}
