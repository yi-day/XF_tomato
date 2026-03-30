package tomato.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 响应封装类
 *
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
public class ApiResponse<T> {

    /** 响应状态码，200 表示成功 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功响应（带数据）
     *
     * @param data 响应数据
     * @return 成功的 ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    /**
     * 返回成功响应（无数据）
     *
     * @return 成功的 ApiResponse
     */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "success", null);
    }

    /**
     * 返回成功响应（自定义消息）
     *
     * @param message 响应消息
     * @return 成功的 ApiResponse
     */
    public static ApiResponse<Void> successMessage(String message) {
        return new ApiResponse<>(200, message, null);
    }

    /**
     * 返回失败响应
     *
     * @param code    错误状态码
     * @param message 错误消息
     * @return 失败的 ApiResponse
     */
    public static ApiResponse<Void> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
