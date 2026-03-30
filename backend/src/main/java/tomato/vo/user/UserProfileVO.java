package tomato.vo.user;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户资料视图对象
 */
@Data
public class UserProfileVO {

    /** 昵称 */
    private String nickname;

    /** 账号 */
    private String account;

    /** 头像文字（昵称首字） */
    private String avatarText;

    /** 总专注分钟数 */
    private Integer totalFocusMinutes;

    /** 总番茄数 */
    private Integer totalPomodoros;

    /** 连续签到天数 */
    private Integer streakDays;

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
