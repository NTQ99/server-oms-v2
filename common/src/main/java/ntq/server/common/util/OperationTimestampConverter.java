package ntq.server.common.util;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OperationTimestampConverter {
  private static final DateTimeFormatter DATE_TIME_FORMATTER
      = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

  private static final ZoneOffset UTC_PLUS7 = ZoneOffset.of("+7");

  public static final JsonReader.ReadObject<OffsetDateTime> JSON_READER = reader -> {
    if (reader.wasNull()) {
      return null;
    }
    var datetime = LocalDateTime.parse(reader.readSimpleString(), DATE_TIME_FORMATTER);
    return OffsetDateTime.of(datetime, UTC_PLUS7);
  };

  public static final JsonWriter.WriteObject<OffsetDateTime> JSON_WRITER = (writer, value) -> {
    if (value == null) {
      writer.writeNull();
    } else {
      writer.writeString(value.format(DATE_TIME_FORMATTER));
    }
  };
}