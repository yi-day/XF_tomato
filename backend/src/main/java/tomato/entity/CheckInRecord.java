package tomato.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签到记录实体类
 */
@Data
public class CheckInRecord {

    /** 记录ID */
    private Long id;

    /** 所属用户ID */
    private Long userId;

    /** 签到日期 */
    private LocalDate checkInDate;

    /** 连续签到天数 */
    private Integer streakDays;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
