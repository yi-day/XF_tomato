package tomato.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomato.common.ApiResponse;
import tomato.service.StatisticsService;
import tomato.vo.statistics.DashboardStatsVO;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardStatsVO> dashboard(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(statisticsService.getDashboard(userId));
    }
}
