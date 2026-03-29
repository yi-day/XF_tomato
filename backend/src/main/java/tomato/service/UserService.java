package tomato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.dto.user.LoginRequest;
import tomato.dto.user.RegisterRequest;
import tomato.dto.user.UpdateProfileRequest;
import tomato.entity.User;
import tomato.mapper.UserMapper;
import tomato.util.PasswordUtils;
import tomato.vo.user.LoginResponse;
import tomato.vo.user.UserProfileVO;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public LoginResponse register(RegisterRequest request) {
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setAvatar("");
        userMapper.insert(user);
        return buildLoginResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || !PasswordUtils.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        return buildLoginResponse(user);
    }

    public UserProfileVO getProfile(Long userId) {
        return convertToProfile(getUserOrThrow(userId));
    }

    public UserProfileVO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserOrThrow(userId);
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        userMapper.updateProfile(user);
        return convertToProfile(getUserOrThrow(userId));
    }

    public User getUserOrThrow(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    private LoginResponse buildLoginResponse(User user) {
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setToken("MVP-" + user.getId());
        return response;
    }

    private UserProfileVO convertToProfile(User user) {
        UserProfileVO profileVO = new UserProfileVO();
        profileVO.setUserId(user.getId());
        profileVO.setUsername(user.getUsername());
        profileVO.setNickname(user.getNickname());
        profileVO.setPhone(user.getPhone());
        profileVO.setAvatar(user.getAvatar());
        profileVO.setCreatedAt(user.getCreatedAt());
        return profileVO;
    }
}
