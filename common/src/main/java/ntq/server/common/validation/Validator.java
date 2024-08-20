package ntq.server.common.validation;

import ntq.server.common.exception.FieldViolation;
import ntq.server.common.exception.ValidateException;

import java.util.ArrayList;
import java.util.List;

public interface Validator<T> {
    default void validate(T t) throws ValidateException {
        var violations = new ArrayList<FieldViolation>();
        validate(t, violations);
        if (!violations.isEmpty()) {
            throw new ValidateException("Invalid object: " + t.getClass(), violations);
        }
    }

    void validate(T t, List<FieldViolation> fieldViolations) throws ValidateException;
}
