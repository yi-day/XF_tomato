package tomato.common;

/**
 * 业务异常类
 * <p>
 * 用于在业务逻辑中抛出可预期的异常，由全局异常处理器统一处理
 * </p>
 */
public class BusinessException extends RuntimeException {

    /** 错误状态码 */
    private final int code;

    /**
     * 构造业务异常（默认状态码 400）
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this(400, message);
    }

    /**
     * 构造业务异常
     *
     * @param code    错误状态码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取错误状态码
     *
     * @return 错误状态码
     */
    public int getCode() {
        return code;
    }
}
