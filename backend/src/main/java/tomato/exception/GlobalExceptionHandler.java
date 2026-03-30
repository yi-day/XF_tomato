package tomato.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tomato.common.ApiResponse;
import tomato.common.BusinessException;

/**
 * 全局异常处理器
 * <p>
 * 统一处理各类异常，将异常转换为标准的 API 响应格式
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param exception 业务异常
     * @return 包含错误信息的 ApiResponse
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException exception) {
        return ApiResponse.fail(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理请求参数校验异常（@RequestBody）
     *
     * @param exception 参数校验异常
     * @return 包含校验错误信息的 ApiResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldError() == null
                ? "参数校验失败"
                : exception.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.fail(400, message);
    }

    /**
     * 处理参数绑定异常
     *
     * @param exception 参数绑定异常
     * @return 包含绑定错误信息的 ApiResponse
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<Void> handleBindException(BindException exception) {
        String message = exception.getBindingResult().getFieldError() == null
                ? "参数绑定失败"
                : exception.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.fail(400, message);
    }

    /**
     * 处理约束违反异常（@RequestParam/@PathVariable）
     *
     * @param exception 约束违反异常
     * @return 包含约束错误信息的 ApiResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        return ApiResponse.fail(400, exception.getMessage());
    }

    /**
     * 处理其他未知异常
     *
     * @param exception 异常
     * @return 包含异常信息的 ApiResponse
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception exception) {
        return ApiResponse.fail(500, exception.getMessage());
    }
}
