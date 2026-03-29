package tomato.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tomato.entity.Task;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Select("""
            select * from tb_task
            where user_id = #{userId}
            order by finished asc, created_at desc
            """)
    List<Task> findByUserId(Long userId);

    @Select("select * from tb_task where id = #{id} and user_id = #{userId}")
    Task findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Insert("""
            insert into tb_task(user_id, title, description, expected_focus_minutes, finished, created_at, updated_at)
            values(#{userId}, #{title}, #{description}, #{expectedFocusMinutes}, #{finished}, now(), now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Task task);

    @Update("""
            update tb_task
            set title = #{title},
                description = #{description},
                expected_focus_minutes = #{expectedFocusMinutes},
                updated_at = now()
            where id = #{id} and user_id = #{userId}
            """)
    int update(Task task);

    @Update("""
            update tb_task
            set finished = #{finished},
                updated_at = now()
            where id = #{id} and user_id = #{userId}
            """)
    int updateFinished(Task task);

    @Delete("delete from tb_task where id = #{id} and user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    @Select("""
            select count(*) from tb_task
            where user_id = #{userId} and finished = true
            """)
    Integer countFinishedByUserId(Long userId);
}



