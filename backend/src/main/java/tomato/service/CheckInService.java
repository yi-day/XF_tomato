package tomato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.entity.CheckInRecord;
import tomato.mapper.CheckInRecordMapper;
import tomato.vo.checkin.CheckInStatusVO;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRecordMapper checkInRecordMapper;
    private final UserService userService;

    public CheckInStatusVO sign(Long userId) {
        userService.getUserOrThrow(userId);
        LocalDate today = LocalDate.now();
        if (checkInRecordMapper.findByUserIdAndDate(userId, today) != null) {
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
        return getStatus(userId);
    }

    public CheckInStatusVO getStatus(Long userId) {
        userService.getUserOrThrow(userId);
        LocalDate today = LocalDate.now();
        CheckInRecord todayRecord = checkInRecordMapper.findByUserIdAndDate(userId, today);
        CheckInRecord latestRecord = checkInRecordMapper.findLatestByUserId(userId);

        CheckInStatusVO statusVO = new CheckInStatusVO();
        statusVO.setCheckedIn(todayRecord != null);
        statusVO.setCurrentStreak(resolveCurrentStreak(latestRecord, today));
        statusVO.setTotalCheckInDays(defaultValue(checkInRecordMapper.countByUserId(userId)));
        statusVO.setLastCheckInDate(latestRecord == null ? null : latestRecord.getCheckInDate());
        return statusVO;
    }

    public Integer getCurrentStreak(Long userId) {
        return getStatus(userId).getCurrentStreak();
    }

    private Integer resolveCurrentStreak(CheckInRecord latestRecord, LocalDate today) {
        if (latestRecord == null) {
            return 0;
        }
        if (latestRecord.getCheckInDate().isEqual(today) || latestRecord.getCheckInDate().plusDays(1).isEqual(today)) {
            return latestRecord.getStreakDays();
        }
        return 0;
    }

    private Integer defaultValue(Integer value) {
        return value == null ? 0 : value;
    }
}
