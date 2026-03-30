package tomato.vo.task;

import lombok.Data;

import java.time.LocalDate;

/**
 * 任务视图对象
 */
@Data
public class TaskVO {

    /** 任务ID */
    private Long id;

    /** 任务标题 */
    private String title;

    /** 优先级 */
    private String priority;

    /** 预估番茄数 */
    private Integer estimatedPomodoros;

    /** 截止日期 */
    private LocalDate deadline;

    /** 是否已完成 */
    private Boolean completed;
}
