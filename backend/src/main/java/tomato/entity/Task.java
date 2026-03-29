package tomato.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Integer expectedFocusMinutes;
    private Boolean finished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
