package tomato.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tomato.entity.User;

/**
 * 用户数据访问层
 */
@Mapper
public interface UserMapper {

    /**
     * 根据账号查询用户
     *
     * @param account 账号
     * @return 用户实体，不存在则返回 null
     */
    @Select("select * from tb_user where account = #{account} limit 1")
    User findByAccount(String account);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体，不存在则返回 null
     */
    @Select("select * from tb_user where id = #{id}")
    User findById(Long id);

    /**
     * 插入新用户
     *
     * @param user 用户实体
     * @return 影响行数
     */
    @Insert("""
            insert into tb_user(account, password, nickname, name, birth_date, school, major, grade, subjects, created_at, updated_at)
            values(#{account}, #{password}, #{nickname}, #{name}, #{birthDate}, #{school}, #{major}, #{grade}, #{subjects}, now(), now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 更新用户资料
     *
     * @param user 用户实体
     * @return 影响行数
     */
    @Update("""
            update tb_user
            set name = #{name},
                birth_date = #{birthDate},
                school = #{school},
                major = #{major},
                grade = #{grade},
                subjects = #{subjects},
                updated_at = now()
            where id = #{id}
            """)
    int updateProfile(User user);
}
