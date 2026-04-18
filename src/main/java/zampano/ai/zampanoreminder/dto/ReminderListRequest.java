package zampano.ai.zampanoreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderListRequest {

    private String name;
    private String color;
    private String icon;
    @Builder.Default
    private Integer sortOrder = 0;
}
