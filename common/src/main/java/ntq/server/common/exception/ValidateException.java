package ntq.server.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidateException extends RuntimeException {
    private final transient List<FieldViolation> fieldViolations;

    public ValidateException(List<FieldViolation> fieldViolations) {
        this(null, fieldViolations);
    }

    public ValidateException(String message, List<FieldViolation> fieldViolations) {
        super(message);
        this.fieldViolations = fieldViolations;
        fieldViolations.forEach(fv -> {
            if (fv.getThrowable() != null) {
                addSuppressed(fv.getThrowable());
            }
        });
    }
}
