package tomato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tomato.common.BusinessException;
import tomato.dto.focus.FocusEndRequest;
import tomato.dto.focus.FocusStartRequest;
import tomato.entity.FocusRecord;
import tomato.mapper.FocusRecordMapper;
import tomato.vo.focus.FocusSessionVO;
import tomato.vo.focus.FocusTodayStatsVO;
import tomato.vo.focus.FocusTrendVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 专注服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FocusService {

    private final FocusRecordMapper focusRecordMapper;
    private final UserService userService;
    private final TaskService taskService;

    /**
     * 开始专注
     *
     * @param userId  用户ID
     * @param request 开始请求
     * @return 专注会话
     */
    public FocusSessionVO start(Long userId, FocusStartRequest request) {
        log.info("[开始专注] userId: {}, mode: {}, taskId: {}", userId, request.getMode(), request.getTaskId());
        userService.getUserOrThrow(userId);
        if (request.getTaskId() != null) {
            taskService.requireTask(userId, request.getTaskId());
        }

        FocusRecord current = focusRecordMapper.findCurrentByUserId(userId);
        if (current != null) {
            log.warn("[开始专注] 用户已有进行中的专注记录, userId: {}, currentRecordId: {}", userId, current.getId());
            throw new BusinessException("当前已有进行中的专注记录");
        }

        FocusRecord record = new FocusRecord();
        record.setUserId(userId);
        record.setTaskId(request.getTaskId());
        record.setMode(request.getMode());
        record.setPlannedMinutes(25); // 默认25分钟
        record.setActualMinutes(0);
        record.setStatus("FOCUSING");
        record.setStartedAt(LocalDateTime.now());
        focusRecordMapper.insert(record);
        log.info("[开始专注] 创建成功, recordId: {}, userId: {}, mode: {}", record.getId(), userId, request.getMode());
        return convertToSessionVO(record);
    }

    /**
     * 结束专注
     *
     * @param userId  用户ID
     * @param request 结束请求
     * @return 专注会话
     */
    public FocusSessionVO end(Long userId, FocusEndRequest request) {
        log.info("[结束专注] userId: {}, sessionId: {}, duration: {}分钟", userId, request.getSessionId(), request.getDuration());
        Long recordId = Long.parseLong(request.getSessionId());
        FocusRecord record = focusRecordMapper.findByIdAndUserId(recordId, userId);
        if (record == null) {
            log.warn("[结束专注] 专注记录不存在, userId: {}, recordId: {}", userId, recordId);
            throw new BusinessException(404, "专注记录不存在");
        }
        if (!"FOCUSING".equals(record.getStatus())) {
            log.warn("[结束专注] 专注记录已结束, userId: {}, recordId: {}, status: {}", userId, recordId, record.getStatus());
            throw new BusinessException("该专注记录已经结束");
        }

        record.setEndedAt(LocalDateTime.now());
        record.setActualMinutes(request.getDuration());
        record.setStatus("COMPLETED");
        focusRecordMapper.finish(record);
        log.info("[结束专注] 结束成功, recordId: {}, 实际时长: {}分钟", recordId, request.getDuration());
        return convertToSessionVO(record);
    }

    /**
     * 获取今日专注统计
     *
     * @param userId 用户ID
     * @return 今日专注统计
     */
    public FocusTodayStatsVO getTodayStats(Long userId) {
        log.debug("[获取今日专注统计] userId: {}", userId);
        userService.getUserOrThrow(userId);
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        FocusTodayStatsVO statsVO = new FocusTodayStatsVO();
        statsVO.setTodayMinutes(defaultValue(focusRecordMapper.sumCompletedMinutesByDateRange(userId, start, end)));
        statsVO.setTodayPomodoros(defaultValue(focusRecordMapper.countCompletedByDateRange(userId, start, end)));
        log.debug("[获取今日专注统计] userId: {}, 今日分钟数: {}, 今日番茄数: {}", 
                userId, statsVO.getTodayMinutes(), statsVO.getTodayPomodoros());
        return statsVO;
    }

    /**
     * 获取近7天专注统计
     *
     * @param userId 用户ID
     * @return 近7天专注统计列表
     */
    public List<FocusTrendVO> getWeeklyStats(Long userId) {
        log.debug("[获取近7天专注趋势] userId: {}", userId);
        userService.getUserOrThrow(userId);
        LocalDate startDate = LocalDate.now().minusDays(6);
        List<Map<String, Object>> dbStats = focusRecordMapper.findWeeklyStats(userId, startDate);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, FocusTrendVO> focusMap = dbStats.stream()
                .collect(Collectors.toMap(
                    m -> m.get("day").toString(),
                    m -> {
                        FocusTrendVO vo = new FocusTrendVO();
                        vo.setDay(m.get("day").toString());
                        vo.setMinutes(((Number) m.get("minutes")).intValue());
                        vo.setPomodoros(((Number) m.get("pomodoros")).intValue());
                        return vo;
                    }
                ));

        List<FocusTrendVO> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            String dayStr = currentDate.format(formatter);
            if (focusMap.containsKey(dayStr)) {
                result.add(focusMap.get(dayStr));
            } else {
                result.add(buildEmptyTrend(currentDate));
            }
        }
        return result;
    }

    /**
     * 获取今日专注分钟数
     *
     * @param userId 用户ID
     * @return 今日专注分钟数
     */
    public Integer getTodayFocusMinutes(Long userId) {
        return getTodayStats(userId).getTodayMinutes();
    }

    /**
     * 获取今日专注次数
     *
     * @param userId 用户ID
     * @return 今日专注次数
     */
    public Integer getTodayFocusCount(Long userId) {
        return getTodayStats(userId).getTodayPomodoros();
    }

    /**
     * 获取高峰时段
     *
     * @param userId 用户ID
     * @return 高峰时段字符串
     */
    public String getPeakPeriod(Long userId) {
        log.debug("[获取高峰时段] userId: {}", userId);
        Integer peakHour = focusRecordMapper.findPeakHour(userId);
        if (peakHour == null) {
            return "暂无数据";
        }
        String period;
        if (peakHour >= 6 && peakHour < 12) {
            period = "上午 " + peakHour + ":00-" + (peakHour + 1) + ":00";
        } else if (peakHour >= 12 && peakHour < 18) {
            period = "下午 " + (peakHour == 12 ? 12 : peakHour - 12) + ":00-" + (peakHour - 11) + ":00";
        } else {
            period = "晚上 " + (peakHour - 12) + ":00-" + (peakHour - 11) + ":00";
        }
        log.debug("[获取高峰时段] userId: {}, peakHour: {}, period: {}", userId, peakHour, period);
        return period;
    }

    /**
     * 构建空的专注趋势数据
     *
     * @param date 日期
     * @return 空的专注趋势对象
     */
    private FocusTrendVO buildEmptyTrend(LocalDate date) {
        FocusTrendVO focusTrendVO = new FocusTrendVO();
        focusTrendVO.setDay(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        focusTrendVO.setMinutes(0);
        focusTrendVO.setPomodoros(0);
        return focusTrendVO;
    }

    /**
     * 空值默认处理
     *
     * @param value 原始值
     * @return 处理后的值
     */
    private Integer defaultValue(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * 转换为专注会话视图对象
     *
     * @param record 专注记录
     * @return 专注会话视图对象
     */
    private FocusSessionVO convertToSessionVO(FocusRecord record) {
        FocusSessionVO sessionVO = new FocusSessionVO();
        sessionVO.setSessionId(String.valueOf(record.getId()));
        sessionVO.setStartTime(record.getStartedAt());
        sessionVO.setMode(record.getMode());
        return sessionVO;
    }
}
