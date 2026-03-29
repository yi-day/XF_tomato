package tomato.vo.focus;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FocusTrendVO {

    private LocalDate focusDate;
    private Integer focusCount;
    private Integer focusMinutes;
}
