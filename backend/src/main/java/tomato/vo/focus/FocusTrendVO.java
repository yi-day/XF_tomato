package tomato.vo.focus;

import lombok.Data;

import java.time.LocalDate;

/**
 * 专注趋势视图对象
 */
@Data
public class FocusTrendVO {

    /** 日期 */
    private String day;

    /** 专注分钟数 */
    private Integer minutes;

    /** 番茄数 */
    private Integer pomodoros;
}
