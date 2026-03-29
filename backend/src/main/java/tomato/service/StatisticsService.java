package tomato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.vo.statistics.DashboardStatsVO;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final FocusService focusService;
    private final TaskService taskService;
    private final CheckInService checkInService;
    private final UserService userService;

    public DashboardStatsVO getDashboard(Long userId) {
        userService.getUserOrThrow(userId);
        DashboardStatsVO dashboardStatsVO = new DashboardStatsVO();
        dashboardStatsVO.setTodayFocusCount(focusService.getTodayFocusCount(userId));
        dashboardStatsVO.setTodayFocusMinutes(focusService.getTodayFocusMinutes(userId));
        dashboardStatsVO.setCompletedTaskCount(taskService.countCompleted(userId));
        dashboardStatsVO.setCurrentCheckInStreak(checkInService.getCurrentStreak(userId));
        dashboardStatsVO.setWeeklyFocusTrend(focusService.getWeeklyTrend(userId));
        return dashboardStatsVO;
    }
}
