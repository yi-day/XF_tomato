package tomato.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tomato.entity.CheckInRecord;

import java.time.LocalDate;

/**
 * 签到记录数据访问层
 */
@Mapper
public interface CheckInRecordMapper {

    /**
     * 查询用户指定日期的签到记录
     *
     * @param userId      用户ID
     * @param checkInDate 签到日期
     * @return 签到记录，不存在则返回 null
     */
    @Select("""
            select * from tb_check_in_record
            where user_id = #{userId} and check_in_date = #{checkInDate}
            limit 1
            """)
    CheckInRecord findByUserIdAndDate(@Param("userId") Long userId, @Param("checkInDate") LocalDate checkInDate);

    /**
     * 查询用户最近一次签到记录
     *
     * @param userId 用户ID
     * @return 签到记录，不存在则返回 null
     */
    @Select("""
            select * from tb_check_in_record
            where user_id = #{userId}
            order by check_in_date desc
            limit 1
            """)
    CheckInRecord findLatestByUserId(Long userId);

    /**
     * 插入签到记录
     *
     * @param record 签到记录实体
     * @return 影响行数
     */
    @Insert("""
            insert into tb_check_in_record(user_id, check_in_date, streak_days, created_at)
            values(#{userId}, #{checkInDate}, #{streakDays}, now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CheckInRecord record);

    /**
     * 统计用户累计签到天数
     *
     * @param userId 用户ID
     * @return 累计签到天数
     */
    @Select("select count(*) from tb_check_in_record where user_id = #{userId}")
    Integer countByUserId(Long userId);
}



