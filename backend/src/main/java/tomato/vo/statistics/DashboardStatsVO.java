package tomato.vo.statistics;

import lombok.Data;

/**
 * 仪表盘统计视图对象
 */
@Data
public class DashboardStatsVO {

    /** 今日专注分钟数 */
    private Integer todayFocusMinutes;

    /** 今日番茄数 */
    private Integer todayPomodoros;

    /** 周完成任务率 */
    private String weekCompletionRate;

    /** 连续签到天数 */
    private Integer streakDays;

    /** 高峰时段 */
    private String peakPeriod;
}
