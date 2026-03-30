package tomato.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 专注记录实体类
 */
@Data
public class FocusRecord {

    /** 记录ID */
    private Long id;

    /** 所属用户ID */
    private Long userId;

    /** 关联任务ID */
    private Long taskId;

    /** 模式: focus, shortBreak, longBreak */
    private String mode;

    /** 计划专注时长（分钟） */
    private Integer plannedMinutes;

    /** 实际专注时长（分钟） */
    private Integer actualMinutes;

    /** 状态：FOCUSING-进行中, COMPLETED-已完成, INTERRUPTED-已中断 */
    private String status;

    /** 开始时间 */
    private LocalDateTime startedAt;

    /** 结束时间 */
    private LocalDateTime endedAt;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
