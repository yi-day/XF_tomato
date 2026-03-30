package tomato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tomato.vo.statistics.DashboardStatsVO;

/**
 * 统计服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final FocusService focusService;
    private final TaskService taskService;
    private final CheckInService checkInService;
    private final UserService userService;

    /**
     * 获取仪表盘统计数据
     *
     * @param userId 用户ID
     * @return 仪表盘统计数据
     */
    public DashboardStatsVO getDashboard(Long userId) {
        log.info("[获取仪表盘统计] userId: {}", userId);
        userService.getUserOrThrow(userId);
        DashboardStatsVO dashboardStatsVO = new DashboardStatsVO();
        dashboardStatsVO.setTodayFocusMinutes(focusService.getTodayFocusMinutes(userId));
        dashboardStatsVO.setTodayPomodoros(focusService.getTodayFocusCount(userId));
        
        // 计算周完成任务率
        Integer completedTasks = taskService.countCompleted(userId);
        Integer totalTasks = taskService.countTotal(userId);
        String completionRate = totalTasks == 0 ? "0%" : 
                String.format("%.0f%%", (completedTasks * 100.0 / totalTasks));
        dashboardStatsVO.setWeekCompletionRate(completionRate);
        
        dashboardStatsVO.setStreakDays(checkInService.getCurrentStreak(userId));
        dashboardStatsVO.setPeakPeriod(focusService.getPeakPeriod(userId));
        
        log.info("[获取仪表盘统计] userId: {}, 今日专注: {}分钟, 今日番茄: {}个, 任务完成率: {}, 连续签到: {}天", 
                userId, dashboardStatsVO.getTodayFocusMinutes(), dashboardStatsVO.getTodayPomodoros(),
                completionRate, dashboardStatsVO.getStreakDays());
        return dashboardStatsVO;
    }
}
