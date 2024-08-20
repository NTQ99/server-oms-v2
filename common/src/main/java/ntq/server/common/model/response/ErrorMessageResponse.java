package ntq.server.common.model.response;

import lombok.Data;
import lombok.experimental.Accessors;
import ntq.server.common.model.ErrorMessageStatus;

import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
public class ErrorMessageResponse {
  private Long id;
  private String topic;
  private int partition;
  private long offset;
  private String key;
  private String message;
  private String error;
  private ErrorMessageStatus status;
  private String createdBy;
  private OffsetDateTime createdAt;
  private String updatedBy;
  private OffsetDateTime updatedAt;
}
