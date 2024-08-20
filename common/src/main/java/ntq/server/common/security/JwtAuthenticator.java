package ntq.server.common.security;

import org.springframework.security.core.Authentication;

public interface JwtAuthenticator {
  Authentication authenticate(String token);
}
