package tomato.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tomato.entity.User;

@Mapper
public interface UserMapper {

    @Select("select * from tb_user where username = #{username} limit 1")
    User findByUsername(String username);

    @Select("select * from tb_user where id = #{id}")
    User findById(Long id);

    @Insert("""
            insert into tb_user(username, password, nickname, phone, avatar, created_at, updated_at)
            values(#{username}, #{password}, #{nickname}, #{phone}, #{avatar}, now(), now())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("""
            update tb_user
            set nickname = #{nickname},
                phone = #{phone},
                avatar = #{avatar},
                updated_at = now()
            where id = #{id}
            """)
    int updateProfile(User user);
}

