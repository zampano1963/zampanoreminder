package zampano.ai.zampanoreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zampano.ai.zampanoreminder.domain.Priority;
import zampano.ai.zampanoreminder.domain.Reminder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReminderResponse {

    private Long id;
    private String title;
    private String notes;
    private String url;
    private LocalDateTime remindAt;
    private Priority priority;
    private boolean flagged;
    private boolean completed;
    private int sortOrder;
    private Long listId;
    private String listName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderResponse from(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .notes(reminder.getNotes())
                .url(reminder.getUrl())
                .remindAt(reminder.getRemindAt())
                .priority(reminder.getPriority())
                .flagged(reminder.isFlagged())
                .completed(reminder.isCompleted())
                .sortOrder(reminder.getSortOrder())
                .listId(reminder.getReminderList() != null ? reminder.getReminderList().getId() : null)
                .listName(reminder.getReminderList() != null ? reminder.getReminderList().getName() : null)
                .createdAt(reminder.getCreatedAt())
                .updatedAt(reminder.getUpdatedAt())
                .build();
    }
}
