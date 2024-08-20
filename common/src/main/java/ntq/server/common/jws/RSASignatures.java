package ntq.server.common.jws;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class RSASignatures {
  public static boolean verify(JWSInput input, PublicKey publicKey) {
    try {
      Signature verifier = getSignature(input.getHeader().getAlgorithm());
      verifier.initVerify(publicKey);
      verifier.update(input.getEncodedSignatureInput().getBytes(StandardCharsets.UTF_8));
      return verifier.verify(input.getSignature());
    } catch (Exception ignored) {
      return false;
    }
  }

  public static boolean verify(byte[] signature, String algorithm, byte[] payload, PublicKey publicKey) {
    try {
      Signature verifier = getSignature(algorithm);
      verifier.initVerify(publicKey);
      verifier.update(payload);
      return verifier.verify(signature);
    } catch (Exception ignored) {
      return false;
    }
  }

  public static byte[] sign(byte[] data, String algorithm, PrivateKey privateKey) {
    try {
      Signature signature = getSignature(algorithm);
      signature.initSign(privateKey);
      signature.update(data);
      return signature.sign();
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't sign, check private key and data", e);
    }
  }

  public static Signature getSignature(String algorithm) {
    try {
      return Signature.getInstance(getJavaAlgorithm(algorithm));
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static String getJavaAlgorithm(String algorithm) {
    return switch (algorithm) {
      case "RS256" -> "SHA256withRSA";
      case "RS384" -> "SHA384withRSA";
      case "RS512" -> "SHA512withRSA";
      default -> throw new IllegalArgumentException("Not an RSA Algorithm");
    };
  }

  private RSASignatures() {
    throw new UnsupportedOperationException();
  }
}
