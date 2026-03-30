package tomato.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tomato.entity.FocusRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 专注记录数据访问层
 */
@Mapper
public interface FocusRecordMapper {

    /**
     * 查询用户当前进行中的专注记录
     *
     * @param userId 用户ID
     * @return 专注记录，不存在则返回 null
     */
    @Select("""
            select * from tb_focus_record
            where user_id = #{userId} and status = 'FOCUSING'
            order by started_at desc
            limit 1
            """)
    FocusRecord findCurrentByUserId(Long userId);

    /**
     * 根据ID查询专注记录
     *
     * @param id 记录ID
     * @return 专注记录，不存在则返回 null
     */
    @Select("select * from tb_focus_record where id = #{id}")
    FocusRecord findById(Long id);

    /**
     * 根据ID和用户ID查询专注记录
     *
     * @param id     记录ID
     * @param userId 用户ID
     * @return 专注记录，不存在则返回 null
     */
    @Select("select * from tb_focus_record where id = #{id} and user_id = #{userId}")
    FocusRecord findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 插入新专注记录
     *
     * @param record 专注记录实体
     * @return 影响行数
     */
    @Insert("""
            insert into tb_focus_record(user_id, task_id, mode, planned_minutes, actual_minutes, status, started_at, ended_at, created_at, updated_at)
            values(#{userId}, #{taskId}, #{mode}, #{plannedMinutes}, #{actualMinutes}, #{status}, #{startedAt}, #{endedAt}, now(), now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FocusRecord record);

    /**
     * 完成专注记录
     *
     * @param record 专注记录实体
     * @return 影响行数
     */
    @Update("""
            update tb_focus_record
            set actual_minutes = #{actualMinutes},
                status = #{status},
                ended_at = #{endedAt},
                updated_at = now()
            where id = #{id}
            """)
    int finish(FocusRecord record);

    /**
     * 统计指定时间范围内已完成的专注次数
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 专注次数
     */
    @Select("""
            select count(*) from tb_focus_record
            where user_id = #{userId}
              and status = 'COMPLETED'
              and started_at >= #{startTime}
              and started_at < #{endTime}
            """)
    Integer countCompletedByDateRange(@Param("userId") Long userId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内已完成的专注分钟数
     *
     * @param userId    用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 专注分钟数
     */
    @Select("""
            select coalesce(sum(actual_minutes), 0) from tb_focus_record
            where user_id = #{userId}
              and status = 'COMPLETED'
              and started_at >= #{startTime}
              and started_at < #{endTime}
            """)
    Integer sumCompletedMinutesByDateRange(@Param("userId") Long userId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 查询近7天专注趋势数据
     *
     * @param userId    用户ID
     * @param startDate 开始日期
     * @return 专注趋势列表，包含 day, minutes, pomodoros
     */
    @Select("""
            select date(started_at) as day,
                   coalesce(sum(actual_minutes), 0) as minutes,
                   count(*) as pomodoros
            from tb_focus_record
            where user_id = #{userId}
              and status = 'COMPLETED'
              and started_at >= #{startDate}
            group by date(started_at)
            order by date(started_at)
            """)
    List<java.util.Map<String, Object>> findWeeklyStats(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);

    /**
     * 查询用户高峰时段
     *
     * @param userId 用户ID
     * @return 高峰时段
     */
    @Select("""
            select hour(started_at) as peak_hour
            from tb_focus_record
            where user_id = #{userId} and status = 'COMPLETED'
            group by hour(started_at)
            order by count(*) desc
            limit 1
            """)
    Integer findPeakHour(@Param("userId") Long userId);
}