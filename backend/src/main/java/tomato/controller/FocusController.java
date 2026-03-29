package tomato.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomato.common.ApiResponse;
import tomato.dto.focus.FocusEndRequest;
import tomato.dto.focus.FocusStartRequest;
import tomato.service.FocusService;
import tomato.vo.focus.FocusSessionVO;
import tomato.vo.focus.FocusTodayStatsVO;
import tomato.vo.focus.FocusTrendVO;

import java.util.List;

@RestController
@RequestMapping("/api/focus")
@RequiredArgsConstructor
public class FocusController {

    private final FocusService focusService;

    @PostMapping("/start")
    public ApiResponse<FocusSessionVO> start(@RequestHeader("X-User-Id") Long userId,
                                             @Valid @RequestBody FocusStartRequest request) {
        return ApiResponse.success(focusService.start(userId, request));
    }

    @PostMapping("/end")
    public ApiResponse<FocusSessionVO> end(@RequestHeader("X-User-Id") Long userId,
                                           @Valid @RequestBody FocusEndRequest request) {
        return ApiResponse.success(focusService.end(userId, request));
    }

    @GetMapping("/today")
    public ApiResponse<FocusTodayStatsVO> today(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(focusService.getTodayStats(userId));
    }

    @GetMapping("/weekly")
    public ApiResponse<List<FocusTrendVO>> weekly(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(focusService.getWeeklyStats(userId));
    }
}
