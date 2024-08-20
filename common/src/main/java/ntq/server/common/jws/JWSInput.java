package ntq.server.common.jws;

import com.dslplatform.json.JsonReader;
import com.google.common.base.Splitter;
import lombok.Data;
import lombok.experimental.Accessors;
import ntq.server.common.util.Json;

import java.util.Base64;

@Data
@Accessors(chain = true)
public class JWSInput {
  private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();
  private static final JsonReader.ReadObject<JWSHeader> JWS_HEADER_READ_OBJECT = Json.findReader(JWSHeader.class);
  private static final Splitter DOT_SPLITTER = Splitter.on('.');
  private String wireString;
  private String encodedHeader;
  private String encodedContent;
  private String encodedSignature;
  private String encodedSignatureInput;
  private JWSHeader header;
  private byte[] content;
  private byte[] signature;

  public JWSInput(String wire) {
    this.wireString = wire;
    var parts = DOT_SPLITTER.splitToList(wire);
    if (parts.size() < 2 || parts.size() > 3) throw new IllegalArgumentException("Parsing error");
    encodedHeader = parts.get(0);
    encodedContent = parts.get(1);
    encodedSignatureInput = encodedHeader + '.' + encodedContent;
    content = BASE64_URL_DECODER.decode(encodedContent);
    if (parts.size() > 2) {
      encodedSignature = parts.get(2);
      signature = BASE64_URL_DECODER.decode(encodedSignature);

    }
    byte[] headerBytes = BASE64_URL_DECODER.decode(encodedHeader);
    header = Json.decode(headerBytes, JWS_HEADER_READ_OBJECT);
  }
}
