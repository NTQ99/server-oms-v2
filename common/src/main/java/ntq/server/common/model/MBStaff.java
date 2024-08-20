package ntq.server.common.model;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@CompiledJson
@Data
@Accessors(chain = true)
public class MBStaff {
  @JsonProperty("preferred_username")
  private String username;
}
