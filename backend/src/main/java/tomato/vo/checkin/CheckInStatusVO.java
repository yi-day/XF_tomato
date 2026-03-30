package tomato.vo.checkin;

import lombok.Data;

/**
 * 签到状态视图对象
 */
@Data
public class CheckInStatusVO {

    /** 今日是否已签到 */
    private Boolean checkedIn;

    /** 连续签到天数 */
    private Integer streakDays;
}
