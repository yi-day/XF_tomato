package tomato.vo.statistics;

import lombok.Data;
import tomato.vo.focus.FocusTrendVO;

import java.util.List;

@Data
public class DashboardStatsVO {

    private Integer todayFocusCount;
    private Integer todayFocusMinutes;
    private Integer completedTaskCount;
    private Integer currentCheckInStreak;
    private List<FocusTrendVO> weeklyFocusTrend;
}
