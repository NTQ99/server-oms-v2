package vn.com.mbbank.steady.core.util;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.runtime.Generics;
import jakarta.persistence.AttributeConverter;
import vn.com.mbbank.steady.common.util.Json;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class EntryIdListConverter implements AttributeConverter<List<Long>, String> {
  private static final JsonWriter.WriteObject<List<Long>> entryIdWriter = Json.findWriter(Generics.makeParameterizedType(List.class, Long.class));
  private static final JsonReader.ReadObject<List<Long>> entryIdReader = Json.findReader(Generics.makeParameterizedType(List.class, Long.class));

  @Override
  public String convertToDatabaseColumn(List<Long> attribute) {
    return attribute != null ? Json.encodeToString(attribute, entryIdWriter) : null;
  }

  @Override
  public List<Long> convertToEntityAttribute(String dbData) {
    return dbData != null ? Json.decode(dbData.getBytes(StandardCharsets.UTF_8), entryIdReader) : null;
  }
}
