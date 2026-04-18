package zampano.ai.zampanoreminder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderListRequest {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Color is required")
    private String color;
    private String icon;
    @Builder.Default
    private Integer sortOrder = 0;
}
