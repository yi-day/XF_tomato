package tomato.vo.focus;

import lombok.Data;

/**
 * 今日专注统计视图对象
 */
@Data
public class FocusTodayStatsVO {

    /** 今日专注分钟数 */
    private Integer todayMinutes;

    /** 今日番茄数 */
    private Integer todayPomodoros;
}
