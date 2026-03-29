package tomato.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tomato.entity.CheckInRecord;

import java.time.LocalDate;

@Mapper
public interface CheckInRecordMapper {

    @Select("""
            select * from tb_check_in_record
            where user_id = #{userId} and check_in_date = #{checkInDate}
            limit 1
            """)
    CheckInRecord findByUserIdAndDate(@Param("userId") Long userId, @Param("checkInDate") LocalDate checkInDate);

    @Select("""
            select * from tb_check_in_record
            where user_id = #{userId}
            order by check_in_date desc
            limit 1
            """)
    CheckInRecord findLatestByUserId(Long userId);

    @Insert("""
            insert into tb_check_in_record(user_id, check_in_date, streak_days, created_at)
            values(#{userId}, #{checkInDate}, #{streakDays}, now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CheckInRecord record);

    @Select("select count(*) from tb_check_in_record where user_id = #{userId}")
    Integer countByUserId(Long userId);
}



