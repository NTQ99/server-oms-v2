package ntq.server.common.jws;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@CompiledJson
@Data
@Accessors(chain = true)
public class JWKSet {
  private List<JWK> keys;
}
