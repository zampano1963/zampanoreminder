package zampano.ai.zampanoreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zampano.ai.zampanoreminder.domain.ReminderList;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReminderListResponse {

    private Long id;
    private String name;
    private String color;
    private String icon;
    private int sortOrder;
    private boolean deletable;
    private int reminderCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderListResponse from(ReminderList list) {
        return ReminderListResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .color(list.getColor())
                .icon(list.getIcon())
                .sortOrder(list.getSortOrder())
                .deletable(list.isDeletable())
                .reminderCount(list.getReminders().size())
                .createdAt(list.getCreatedAt())
                .updatedAt(list.getUpdatedAt())
                .build();
    }
}
