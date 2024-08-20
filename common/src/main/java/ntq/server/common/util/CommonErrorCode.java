package ntq.server.common.util;

import ntq.server.common.exception.BusinessErrorCode;

public interface CommonErrorCode {
  BusinessErrorCode INTERNAL_SERVER_ERROR =
      new BusinessErrorCode(5000, "Internal server error", 500);
  BusinessErrorCode INTERNAL_SERVICE_ERROR =
      new BusinessErrorCode(5001, "Internal service error", 500);
  BusinessErrorCode INTERNAL_DATABASE_ERROR =
      new BusinessErrorCode(5002, "Internal database error", 500);
  BusinessErrorCode INVALID_PARAMETERS =
      new BusinessErrorCode(4000, "Invalid parameters", 400);
  BusinessErrorCode UNAUTHORIZED =
      new BusinessErrorCode(4001, "You need to login to to access this resource", 401);
  BusinessErrorCode FORBIDDEN =
      new BusinessErrorCode(4002, "You don't have permission to to access this resource", 403);
  BusinessErrorCode ERROR_MESSAGE_NOT_FOUND =
      new BusinessErrorCode(4003, "Error message not found", 404);
}
