package zampano.ai.zampanoreminder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zampano.ai.zampanoreminder.domain.Priority;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String notes;
    private String url;
    private LocalDateTime remindAt;
    @Builder.Default
    private Priority priority = Priority.NONE;
    @Builder.Default
    private Boolean flagged = false;
    private Long listId;
}
