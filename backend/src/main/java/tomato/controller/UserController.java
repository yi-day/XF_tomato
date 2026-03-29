package tomato.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tomato.common.ApiResponse;
import tomato.dto.user.LoginRequest;
import tomato.dto.user.RegisterRequest;
import tomato.dto.user.UpdateProfileRequest;
import tomato.service.UserService;
import tomato.vo.user.LoginResponse;
import tomato.vo.user.UserProfileVO;

@Validated
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(userService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(userService.login(request));
    }

    @GetMapping("/profile")
    public ApiResponse<UserProfileVO> getProfile(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(userService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ApiResponse<UserProfileVO> updateProfile(@RequestHeader("X-User-Id") Long userId,
                                                    @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.success(userService.updateProfile(userId, request));
    }
}
