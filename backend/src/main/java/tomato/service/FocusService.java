package tomato.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.dto.focus.FocusEndRequest;
import tomato.dto.focus.FocusStartRequest;
import tomato.entity.FocusRecord;
import tomato.mapper.FocusRecordMapper;
import tomato.vo.focus.FocusSessionVO;
import tomato.vo.focus.FocusTodayStatsVO;
import tomato.vo.focus.FocusTrendVO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FocusService {

    private final FocusRecordMapper focusRecordMapper;
    private final UserService userService;
    private final TaskService taskService;

    public FocusSessionVO start(Long userId, FocusStartRequest request) {
        userService.getUserOrThrow(userId);
        if (request.getTaskId() != null) {
            taskService.requireTask(userId, request.getTaskId());
        }

        FocusRecord current = focusRecordMapper.findCurrentByUserId(userId);
        if (current != null) {
            throw new BusinessException("当前已有进行中的专注记录");
        }

        FocusRecord record = new FocusRecord();
        record.setUserId(userId);
        record.setTaskId(request.getTaskId());
        record.setPlannedMinutes(request.getPlannedMinutes() == null ? 25 : request.getPlannedMinutes());
        record.setActualMinutes(0);
        record.setStatus("FOCUSING");
        record.setStartedAt(LocalDateTime.now());
        focusRecordMapper.insert(record);
        return convertToSessionVO(focusRecordMapper.findByIdAndUserId(record.getId(), userId));
    }

    public FocusSessionVO end(Long userId, FocusEndRequest request) {
        FocusRecord record = focusRecordMapper.findByIdAndUserId(request.getFocusRecordId(), userId);
        if (record == null) {
            throw new BusinessException(404, "专注记录不存在");
        }
        if (!"FOCUSING".equals(record.getStatus())) {
            throw new BusinessException("该专注记录已经结束");
        }

        record.setEndedAt(LocalDateTime.now());
        record.setActualMinutes(resolveActualMinutes(record, request.getActualMinutes()));
        record.setStatus(Boolean.TRUE.equals(request.getInterrupted()) ? "INTERRUPTED" : "COMPLETED");
        focusRecordMapper.finish(record);
        return convertToSessionVO(focusRecordMapper.findByIdAndUserId(record.getId(), userId));
    }

    public FocusTodayStatsVO getTodayStats(Long userId) {
        userService.getUserOrThrow(userId);
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        FocusTodayStatsVO statsVO = new FocusTodayStatsVO();
        statsVO.setFocusCount(defaultValue(focusRecordMapper.countCompletedByDateRange(userId, start, end)));
        statsVO.setFocusMinutes(defaultValue(focusRecordMapper.sumCompletedMinutesByDateRange(userId, start, end)));
        return statsVO;
    }

    public List<FocusTrendVO> getWeeklyStats(Long userId) {
        userService.getUserOrThrow(userId);
        LocalDate startDate = LocalDate.now().minusDays(6);
        List<FocusTrendVO> dbStats = focusRecordMapper.findWeeklyStats(userId, startDate);
        Map<LocalDate, FocusTrendVO> focusMap = dbStats.stream()
                .collect(Collectors.toMap(FocusTrendVO::getFocusDate, item -> item));

        List<FocusTrendVO> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            result.add(focusMap.getOrDefault(currentDate, buildEmptyTrend(currentDate)));
        }
        return result;
    }

    public Integer getTodayFocusMinutes(Long userId) {
        return getTodayStats(userId).getFocusMinutes();
    }

    public Integer getTodayFocusCount(Long userId) {
        return getTodayStats(userId).getFocusCount();
    }

    public List<FocusTrendVO> getWeeklyTrend(Long userId) {
        return getWeeklyStats(userId);
    }

    private FocusTrendVO buildEmptyTrend(LocalDate date) {
        FocusTrendVO focusTrendVO = new FocusTrendVO();
        focusTrendVO.setFocusDate(date);
        focusTrendVO.setFocusCount(0);
        focusTrendVO.setFocusMinutes(0);
        return focusTrendVO;
    }

    private Integer resolveActualMinutes(FocusRecord record, Integer actualMinutes) {
        if (actualMinutes != null && actualMinutes > 0) {
            return actualMinutes;
        }
        long minutes = Duration.between(record.getStartedAt(), LocalDateTime.now()).toMinutes();
        return Math.max(1, (int) minutes);
    }

    private Integer defaultValue(Integer value) {
        return value == null ? 0 : value;
    }

    private FocusSessionVO convertToSessionVO(FocusRecord record) {
        FocusSessionVO sessionVO = new FocusSessionVO();
        sessionVO.setId(record.getId());
        sessionVO.setTaskId(record.getTaskId());
        sessionVO.setPlannedMinutes(record.getPlannedMinutes());
        sessionVO.setActualMinutes(record.getActualMinutes());
        sessionVO.setStatus(record.getStatus());
        sessionVO.setStartedAt(record.getStartedAt());
        sessionVO.setEndedAt(record.getEndedAt());
        return sessionVO;
    }
}
