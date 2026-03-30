package tomato.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务实体类
 */
@Data
public class Task {

    /** 任务ID */
    private Long id;

    /** 所属用户ID */
    private Long userId;

    /** 任务标题 */
    private String title;

    /** 优先级: HIGH, MEDIUM, LOW */
    private String priority;

    /** 预估番茄数 */
    private Integer estimatedPomodoros;

    /** 截止日期 */
    private LocalDate deadline;

    /** 是否已完成 */
    private Boolean completed;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
