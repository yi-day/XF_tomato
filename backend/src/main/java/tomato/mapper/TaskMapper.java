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

/**
 * 任务数据访问层
 */
@Mapper
public interface TaskMapper {

    /**
     * 查询用户的所有任务，按完成状态和创建时间排序
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    @Select("""
            select * from tb_task
            where user_id = #{userId}
            order by completed asc, created_at desc
            """)
    List<Task> findByUserId(Long userId);

    /**
     * 根据ID和用户ID查询任务
     *
     * @param id     任务ID
     * @param userId 用户ID
     * @return 任务实体，不存在则返回 null
     */
    @Select("select * from tb_task where id = #{id} and user_id = #{userId}")
    Task findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 插入新任务
     *
     * @param task 任务实体
     * @return 影响行数
     */
    @Insert("""
            insert into tb_task(user_id, title, priority, estimated_pomodoros, deadline, completed, created_at, updated_at)
            values(#{userId}, #{title}, #{priority}, #{estimatedPomodoros}, #{deadline}, #{completed}, now(), now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Task task);

    /**
     * 更新任务信息
     *
     * @param task 任务实体
     * @return 影响行数
     */
    @Update("""
            update tb_task
            set title = #{title},
                priority = #{priority},
                estimated_pomodoros = #{estimatedPomodoros},
                deadline = #{deadline},
                updated_at = now()
            where id = #{id} and user_id = #{userId}
            """)
    int update(Task task);

    /**
     * 更新任务完成状态
     *
     * @param task 任务实体
     * @return 影响行数
     */
    @Update("""
            update tb_task
            set completed = #{completed},
                updated_at = now()
            where id = #{id} and user_id = #{userId}
            """)
    int updateCompleted(Task task);

    /**
     * 删除任务
     *
     * @param id     任务ID
     * @param userId 用户ID
     * @return 影响行数
     */
    @Delete("delete from tb_task where id = #{id} and user_id = #{userId}")
    int delete(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 统计用户已完成任务数
     *
     * @param userId 用户ID
     * @return 已完成任务数
     */
    @Select("""
            select count(*) from tb_task
            where user_id = #{userId} and completed = true
            """)
    Integer countCompletedByUserId(Long userId);

    /**
     * 统计用户任务总数
     *
     * @param userId 用户ID
     * @return 任务总数
     */
    @Select("select count(*) from tb_task where user_id = #{userId}")
    Integer countByUserId(Long userId);
}