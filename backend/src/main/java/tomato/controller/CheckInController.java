package tomato.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomato.common.ApiResponse;
import tomato.service.CheckInService;
import tomato.vo.checkin.CheckInStatusVO;

@RestController
@RequestMapping("/api/checkin")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/sign")
    public ApiResponse<CheckInStatusVO> sign(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(checkInService.sign(userId));
    }

    @GetMapping("/status")
    public ApiResponse<CheckInStatusVO> status(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(checkInService.getStatus(userId));
    }
}
