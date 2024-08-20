package ntq.server.common.model.response;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonInclude;

import ntq.server.common.exception.BusinessErrorCode;
import ntq.server.common.exception.BusinessException;
import ntq.server.common.exception.FieldViolation;
import ntq.server.common.util.Constant;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private T data;
    private Metadata meta = new Metadata();

    @CompiledJson
    Response(T data, Metadata meta) {
        this.data = data;
        this.meta = meta;
    }

    public Response() {
    }

    public static <T> Response<T> ofSucceeded() {
        return ofSucceeded((T) null);
    }

    @SuppressWarnings("unchecked")
    public static <T> Response<T> ofSucceeded(T data) {
        Response<T> response = new Response<>();
        response.data = data;
        response.meta.code = Metadata.OK_CODE;
        return response;
    }

    public static <T> Response<List<T>> ofSucceeded(Page<T> data) {
        Response<List<T>> response = new Response<>();
        response.data = data.getContent();
        response.meta.code = Metadata.OK_CODE;
        response.meta.page = data.getNumber();
        response.meta.size = data.getSize();
        response.meta.total = data.getTotalElements();
        return response;
    }

    public static Response<Void> ofFailed(BusinessErrorCode errorCode) {
        return ofFailed(errorCode, (String) null);
    }

    public static Response<Void> ofFailed(BusinessErrorCode errorCode, List<FieldViolation> errors) {
        return ofFailed(errorCode, null, errors);
    }

    public static Response<Void> ofFailed(BusinessErrorCode errorCode, String message) {
        return ofFailed(errorCode, message, null);
    }

    public static Response<Void> ofFailed(BusinessErrorCode errorCode, String message, List<FieldViolation> errors) {
        Response<Void> response = new Response<>();
        response.meta.code = Constant.PREFIX_RESPONSE_CODE + errorCode.getCode();
        response.meta.message = message != null ? message : errorCode.getMessage();
        response.meta.errors = errors;
        return response;
    }

    public static Response<Void> ofFailed(BusinessException exception) {
        return ofFailed(exception.getErrorCode(), exception.getMessage());
    }

    public T getData() {
        return data;
    }

    public Metadata getMeta() {
        return meta;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Metadata {
        public static final String OK_CODE = Constant.PREFIX_RESPONSE_CODE + 200;
        String code;
        Integer page;
        Integer size;
        Long total;
        String message;
        List<FieldViolation> errors;

        public Metadata() {
        }

        @CompiledJson
        public Metadata(String code, Integer page, Integer size, Long total, String message, List<FieldViolation> errors) {
            this.code = code;
            this.page = page;
            this.size = size;
            this.total = total;
            this.message = message;
            this.errors = errors;
        }

        public String getCode() {
            return code;
        }

        public Integer getPage() {
            return page;
        }

        public Integer getSize() {
            return size;
        }

        public Long getTotal() {
            return total;
        }

        public String getMessage() {
            return message;
        }

        public List<FieldViolation> getErrors() {
            return errors;
        }
    }
}