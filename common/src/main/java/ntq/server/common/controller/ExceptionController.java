package ntq.server.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ntq.server.common.exception.BusinessErrorCode;
import ntq.server.common.exception.BusinessException;
import ntq.server.common.exception.FieldViolation;
import ntq.server.common.exception.ValidateException;
import ntq.server.common.model.response.Response;
import ntq.server.common.util.CommonErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletionException;

@Log4j2
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(BusinessException.class)
    protected void handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, e.getErrorCode(), request, response);
    }

    @ExceptionHandler(Exception.class)
    protected void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, CommonErrorCode.INTERNAL_SERVER_ERROR, request, response);
    }

    @ExceptionHandler(CompletionException.class)
    protected void handleCompletionException(CompletionException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (e.getCause() instanceof BusinessException be) {
            handleBusinessException(be, request, response);
        } else {
            handle(e.getCause(), CommonErrorCode.INTERNAL_SERVER_ERROR, request, response);
        }
    }

    @ExceptionHandler(BindException.class)
    protected void handleBindException(BindException e, HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        var fieldViolations = e.getBindingResult().getAllErrors().stream()
                .map(error -> new FieldViolation(((FieldError) error).getField(), error.getDefaultMessage()))
                .toList();

        handleInvalidParams(e, fieldViolations, request, response);
    }

    @ExceptionHandler(ValidateException.class)
    protected void handleValidateException(ValidateException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleInvalidParams(e, e.getFieldViolations(), request, response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected void handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, CommonErrorCode.INVALID_PARAMETERS, request, response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected void handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var fieldViolations = List.of(new FieldViolation(e.getParameterName(), e.getMessage()));
        handleInvalidParams(e, fieldViolations, request, response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected void handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var fieldViolations = List.of(new FieldViolation(e.getName(), e.getMessage()));
        handleInvalidParams(e, fieldViolations, request, response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, CommonErrorCode.FORBIDDEN, request, response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, CommonErrorCode.UNAUTHORIZED, request, response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        handle(e, CommonErrorCode.INVALID_PARAMETERS, request, response);
    }

    private void handle(Throwable t, BusinessErrorCode errorCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var errorResponse = Response.ofFailed(errorCode, t.getMessage());
        writeResponse(request, response, errorCode.getHttpStatus(), errorResponse, t);
    }

    private void handleInvalidParams(Exception e, List<FieldViolation> fieldViolations, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var errorResponse = Response.ofFailed(CommonErrorCode.INVALID_PARAMETERS, e.getMessage(), fieldViolations);
        writeResponse(request, response, CommonErrorCode.INVALID_PARAMETERS.getHttpStatus(), errorResponse, e);
    }

    private void writeResponse(HttpServletRequest request, HttpServletResponse servletResponse, int httpStatus, Response<?> errorResponse, Throwable t) throws IOException {
        servletResponse.setStatus(httpStatus);
        servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] body = objectMapper.writeValueAsBytes(errorResponse);
        log.error(() -> createMessage(request, body), t);
        servletResponse.setContentLength(body.length);
        servletResponse.getOutputStream().write(body);
    }

    private String createMessage(HttpServletRequest request, byte[] errorResponse) {
        var msg = new StringBuilder();
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getRequestURI());
        var queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }
        var payload = getMessagePayload(request);
        if (payload != null) {
            msg.append(", payload=").append(payload);
        }
        msg.append(", response=").append(new String(errorResponse, StandardCharsets.UTF_8));
        return msg.toString();
    }

    private String getMessagePayload(HttpServletRequest request) {
        var wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            var buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 10000);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }
}