package tomato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.dto.user.LoginRequest;
import tomato.dto.user.RegisterRequest;
import tomato.dto.user.UpdateProfileRequest;
import tomato.entity.User;
import tomato.mapper.FocusRecordMapper;
import tomato.mapper.UserMapper;
import tomato.util.PasswordUtils;
import tomato.vo.user.LoginResponse;
import tomato.vo.user.UserProfileVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final FocusRecordMapper focusRecordMapper;

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 登录响应
     */
    public LoginResponse register(RegisterRequest request) {
        log.info("[用户注册] 开始注册, account: {}, nickname: {}", request.getAccount(), request.getNickname());
        User existingUser = userMapper.findByAccount(request.getAccount());
        if (existingUser != null) {
            log.warn("[用户注册] 账号已存在, account: {}", request.getAccount());
            throw new BusinessException("账号已存在");
        }

        User user = new User();
        user.setAccount(request.getAccount());
        user.setPassword(PasswordUtils.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        userMapper.insert(user);
        log.info("[用户注册] 注册成功, userId: {}, account: {}", user.getId(), user.getAccount());
        return buildLoginResponse(user);
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    public LoginResponse login(LoginRequest request) {
        log.info("[用户登录] 开始登录, account: {}", request.getAccount());
        User user = userMapper.findByAccount(request.getAccount());
        if (user == null || !PasswordUtils.matches(request.getPassword(), user.getPassword())) {
            log.warn("[用户登录] 登录失败, account: {}", request.getAccount());
            throw new BusinessException("账号或密码错误");
        }
        log.info("[用户登录] 登录成功, userId: {}, account: {}", user.getId(), user.getAccount());
        return buildLoginResponse(user);
    }

    /**
     * 获取用户资料
     *
     * @param userId 用户ID
     * @return 用户资料
     */
    public UserProfileVO getProfile(Long userId) {
        log.info("[获取用户资料] userId: {}", userId);
        return convertToProfile(getUserOrThrow(userId));
    }

    /**
     * 更新用户资料
     *
     * @param userId  用户ID
     * @param request 更新请求
     * @return 更新后的用户资料
     */
    public UserProfileVO updateProfile(Long userId, UpdateProfileRequest request) {
        log.info("[更新用户资料] userId: {}, request: {}", userId, request);
        User user = getUserOrThrow(userId);
        user.setName(request.getName());
        user.setBirthDate(request.getBirthDate());
        user.setSchool(request.getSchool());
        user.setMajor(request.getMajor());
        user.setGrade(request.getGrade());
        user.setSubjects(request.getSubjects());
        userMapper.updateProfile(user);
        log.info("[更新用户资料] 更新成功, userId: {}", userId);
        return convertToProfile(getUserOrThrow(userId));
    }

    /**
     * 获取用户，不存在则抛出异常
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    public User getUserOrThrow(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    /**
     * 构建登录响应
     *
     * @param user 用户实体
     * @return 登录响应
     */
    private LoginResponse buildLoginResponse(User user) {
        LoginResponse response = new LoginResponse();
        response.setUserId(String.valueOf(user.getId()));
        response.setToken("MVP-" + user.getId());
        response.setNickname(user.getNickname());
        return response;
    }

    /**
     * 转换为用户资料视图对象
     *
     * @param user 用户实体
     * @return 用户资料视图对象
     */
    private UserProfileVO convertToProfile(User user) {
        UserProfileVO profileVO = new UserProfileVO();
        profileVO.setNickname(user.getNickname());
        profileVO.setAccount(user.getAccount());
        profileVO.setAvatarText(getAvatarText(user.getNickname()));
        
        // 计算总专注分钟数和番茄数
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        Integer totalMinutes = focusRecordMapper.sumCompletedMinutesByDateRange(user.getId(), 
                LocalDate.of(2020, 1, 1).atStartOfDay(), LocalDateTime.now());
        Integer totalPomodoros = focusRecordMapper.countCompletedByDateRange(user.getId(), 
                LocalDate.of(2020, 1, 1).atStartOfDay(), LocalDateTime.now());
        
        profileVO.setTotalFocusMinutes(totalMinutes != null ? totalMinutes : 0);
        profileVO.setTotalPomodoros(totalPomodoros != null ? totalPomodoros : 0);
        profileVO.setStreakDays(0); // 将由 CheckInService 计算设置
        
        profileVO.setName(user.getName());
        profileVO.setBirthDate(user.getBirthDate());
        profileVO.setSchool(user.getSchool());
        profileVO.setMajor(user.getMajor());
        profileVO.setGrade(user.getGrade());
        profileVO.setSubjects(user.getSubjects());
        return profileVO;
    }

    /**
     * 获取头像文字（昵称首字）
     */
    private String getAvatarText(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return "T";
        }
        return nickname.substring(0, 1);
    }
}
