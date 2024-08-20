package ntq.server.common.util;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonConverter;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import lombok.Data;

@JsonConverter(target = Void.class)
public class VoidDslConverter {
  public static final JsonReader.ReadObject<Void> JSON_READER = reader -> {
    if (reader.wasNull()) return null;
    throw new IllegalArgumentException("expect null value");
  };
  public static final JsonWriter.WriteObject<Void> JSON_WRITER = (writer, value) -> writer.writeNull();

  @CompiledJson
  @Data
  public static class DummyModel {
    private Void v;
  }

  private VoidDslConverter() {
    throw new UnsupportedOperationException();
  }
}