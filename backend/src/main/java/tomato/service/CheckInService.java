package tomato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.entity.CheckInRecord;
import tomato.mapper.CheckInRecordMapper;
import tomato.vo.checkin.CheckInStatusVO;

import java.time.LocalDate;

/**
 * 签到服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRecordMapper checkInRecordMapper;
    private final UserService userService;

    /**
     * 用户签到
     *
     * @param userId 用户ID
     * @return 签到状态
     */
    public CheckInStatusVO sign(Long userId) {
        log.info("[用户签到] userId: {}", userId);
        userService.getUserOrThrow(userId);
        LocalDate today = LocalDate.now();
        if (checkInRecordMapper.findByUserIdAndDate(userId, today) != null) {
            log.warn("[用户签到] 今日已签到, userId: {}", userId);
            throw new BusinessException("今天已经签到过了");
        }

        CheckInRecord latestRecord = checkInRecordMapper.findLatestByUserId(userId);
        int streakDays = 1;
        if (latestRecord != null && latestRecord.getCheckInDate().plusDays(1).isEqual(today)) {
            streakDays = latestRecord.getStreakDays() + 1;
        }

        CheckInRecord record = new CheckInRecord();
        record.setUserId(userId);
        record.setCheckInDate(today);
        record.setStreakDays(streakDays);
        checkInRecordMapper.insert(record);
        log.info("[用户签到] 签到成功, userId: {}, streakDays: {}", userId, streakDays);
        return getStatus(userId);
    }

    /**
     * 获取签到状态
     *
     * @param userId 用户ID
     * @return 签到状态
     */
    public CheckInStatusVO getStatus(Long userId) {
        log.debug("[获取签到状态] userId: {}", userId);
        userService.getUserOrThrow(userId);
        LocalDate today = LocalDate.now();
        CheckInRecord todayRecord = checkInRecordMapper.findByUserIdAndDate(userId, today);
        CheckInRecord latestRecord = checkInRecordMapper.findLatestByUserId(userId);

        CheckInStatusVO statusVO = new CheckInStatusVO();
        statusVO.setCheckedIn(todayRecord != null);
        statusVO.setStreakDays(resolveCurrentStreak(latestRecord, today));
        log.debug("[获取签到状态] userId: {}, checkedIn: {}, streakDays: {}", 
                userId, statusVO.getCheckedIn(), statusVO.getStreakDays());
        return statusVO;
    }

    /**
     * 获取当前连续签到天数
     *
     * @param userId 用户ID
     * @return 当前连续签到天数
     */
    public Integer getCurrentStreak(Long userId) {
        return getStatus(userId).getStreakDays();
    }

    /**
     * 计算当前连续签到天数
     *
     * @param latestRecord 最近签到记录
     * @param today        今天日期
     * @return 连续签到天数
     */
    private Integer resolveCurrentStreak(CheckInRecord latestRecord, LocalDate today) {
        if (latestRecord == null) {
            return 0;
        }
        if (latestRecord.getCheckInDate().isEqual(today) || latestRecord.getCheckInDate().plusDays(1).isEqual(today)) {
            return latestRecord.getStreakDays();
        }
        return 0;
    }
}
