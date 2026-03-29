package tomato.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CheckInRecord {

    private Long id;
    private Long userId;
    private LocalDate checkInDate;
    private Integer streakDays;
    private LocalDateTime createdAt;
}
