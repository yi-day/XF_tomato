package tomato.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {

    /** 用户ID */
    private Long id;

    /** 账号 */
    private String account;

    /** 密码（加密后） */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 真实姓名 */
    private String name;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 学校 */
    private String school;

    /** 专业 */
    private String major;

    /** 年级 */
    private String grade;

    /** 学科 */
    private String subjects;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
