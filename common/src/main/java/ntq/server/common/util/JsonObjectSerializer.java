package ntq.server.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class JsonObjectSerializer extends JsonSerializer<JsonObject> {
  @Override
  public void serialize(JsonObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    jgen.writeObject(value.getMap());
  }
}
