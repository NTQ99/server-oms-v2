package ntq.server.common.jws;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@CompiledJson
@Data
@Accessors(chain = true)
public class JWK {
  public enum Use {
    SIG("sig"), ENCRYPTION("enc");

    private final String value;

    Use(String value) {
      this.value = value;
    }

    @JsonValue
    public String asString() {
      return value;
    }
  }

  @JsonProperty("kid")
  private String keyId;
  @JsonProperty("kty")
  private String keyType;
  @JsonProperty("alg")
  private String algorithm;
  @JsonProperty("use")
  private Use publicKeyUse;
  @JsonProperty("n")
  private String modulus;
  @JsonProperty("e")
  private String publicExponent;

  public PublicKey toPublicKey() {
    if (keyType.equals(KeyType.RSA)) {
      return createRSAPublicKey();
    }
    throw new IllegalArgumentException("Unsupported keyType " + keyType);
  }

  private PublicKey createRSAPublicKey() {
    BigInteger modulusNumber = new BigInteger(1, Base64.getUrlDecoder().decode(modulus));
    BigInteger publicExponentNumber = new BigInteger(1, Base64.getUrlDecoder().decode(publicExponent));
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePublic(new RSAPublicKeySpec(modulusNumber, publicExponentNumber));
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}

