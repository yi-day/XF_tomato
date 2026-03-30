package tomato.dto.user;

import lombok.Data;

import java.time.LocalDate;

/**
 * 更新用户资料请求
 */
@Data
public class UpdateProfileRequest {

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
}
