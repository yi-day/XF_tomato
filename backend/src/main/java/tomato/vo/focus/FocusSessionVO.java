package tomato.vo.focus;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 专注会话视图对象
 */
@Data
public class FocusSessionVO {

    /** 会话ID */
    private String sessionId;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 模式 */
    private String mode;
}
