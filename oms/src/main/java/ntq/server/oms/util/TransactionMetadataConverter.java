package vn.com.mbbank.steady.core.util;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import jakarta.persistence.AttributeConverter;
import vn.com.mbbank.steady.common.util.Json;
import vn.com.mbbank.steady.core.model.TransactionMetadata;

import java.nio.charset.StandardCharsets;

public class TransactionMetadataConverter implements AttributeConverter<TransactionMetadata, String> {
  private static final JsonWriter.WriteObject<TransactionMetadata> txnMetadataWriter = Json.findWriter(TransactionMetadata.class);
  private static final JsonReader.ReadObject<TransactionMetadata> txnMetadataReader = Json.findReader(TransactionMetadata.class);

  @Override
  public String convertToDatabaseColumn(TransactionMetadata attribute) {
    return attribute != null ? Json.encodeToString(attribute, txnMetadataWriter) : null;
  }

  @Override
  public TransactionMetadata convertToEntityAttribute(String dbData) {
    return dbData != null ? Json.decode(dbData.getBytes(StandardCharsets.UTF_8), txnMetadataReader) : null;
  }
}
