package tomato.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tomato.entity.FocusRecord;
import tomato.vo.focus.FocusTrendVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FocusRecordMapper {

    @Select("""
            select * from tb_focus_record
            where user_id = #{userId} and status = 'FOCUSING'
            order by started_at desc
            limit 1
            """)
    FocusRecord findCurrentByUserId(Long userId);

    @Select("select * from tb_focus_record where id = #{id} and user_id = #{userId}")
    FocusRecord findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("""
            insert into tb_focus_record(user_id, task_id, planned_minutes, actual_minutes, status, started_at, ended_at, created_at, updated_at)
            values(#{userId}, #{taskId}, #{plannedMinutes}, #{actualMinutes}, #{status}, #{startedAt}, #{endedAt}, now(), now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FocusRecord record);

    @Update("""
            update tb_focus_record
            set actual_minutes = #{actualMinutes},
                status = #{status},
                ended_at = #{endedAt},
                updated_at = now()
            where id = #{id} and user_id = #{userId}
            """)
    int finish(FocusRecord record);

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

    @Select("""
            select date(started_at) as focusDate,
                   count(*) as focusCount,
                   coalesce(sum(actual_minutes), 0) as focusMinutes
            from tb_focus_record
            where user_id = #{userId}
              and status = 'COMPLETED'
              and started_at >= #{startDate}
            group by date(started_at)
            order by date(started_at)
            """)
    List<FocusTrendVO> findWeeklyStats(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);
}



