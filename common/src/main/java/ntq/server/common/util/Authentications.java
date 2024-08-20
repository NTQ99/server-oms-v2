package ntq.server.common.util;

import ntq.server.common.exception.BusinessException;
import ntq.server.common.model.MBStaff;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class Authentications {
  private Authentications() {
  }

  public static String requireUsername() {
    return requireUsername(SecurityContextHolder.getContext().getAuthentication());
  }

  public static String requireUsername(Authentication authentication) {
    return requireMBStaff(authentication).getUsername();
  }

  public static MBStaff requireMBStaff() {
    return requireMBStaff(SecurityContextHolder.getContext().getAuthentication());
  }

  public static MBStaff requireMBStaff(Authentication authentication) {
    if (authentication != null && authentication.getPrincipal() instanceof MBStaff staff) {
      return staff;
    }
    throw new BusinessException(CommonErrorCode.FORBIDDEN, "You don't have permission to access this resource. Require logged user");
  }

  public static Optional<MBStaff> getMBStaff() {
    return getMBStaff(SecurityContextHolder.getContext().getAuthentication());
  }

  public static Optional<MBStaff> getMBStaff(Authentication authentication) {
    if (authentication != null && authentication.getPrincipal() instanceof MBStaff staff) {
      return Optional.of(staff);
    }
    return Optional.empty();
  }

  public static Optional<String> getUsername() {
    return getUsername(SecurityContextHolder.getContext().getAuthentication());
  }

  public static Optional<String> getUsername(Authentication authentication) {
    if (authentication != null && authentication.getPrincipal() instanceof MBStaff staff) {
      return Optional.of(staff.getUsername());
    }
    return Optional.empty();
  }
}
