package tomato.vo.checkin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckInStatusVO {

    private Boolean checkedIn;
    private Integer currentStreak;
    private Integer totalCheckInDays;
    private LocalDate lastCheckInDate;
}
