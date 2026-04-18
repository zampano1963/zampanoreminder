package zampano.ai.zampanoreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderTest {

    @Test
    @DisplayName("Builder로 Reminder를 생성할 수 있다")
    void createWithBuilder() {
        Reminder reminder = Reminder.builder()
                .title("Meeting")
                .notes("Team standup")
                .url("https://meet.google.com")
                .remindAt(LocalDateTime.of(2026, 4, 18, 10, 0))
                .priority(Priority.HIGH)
                .flagged(true)
                .build();

        assertThat(reminder.getTitle()).isEqualTo("Meeting");
        assertThat(reminder.getNotes()).isEqualTo("Team standup");
        assertThat(reminder.getUrl()).isEqualTo("https://meet.google.com");
        assertThat(reminder.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(reminder.isFlagged()).isTrue();
        assertThat(reminder.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("기본값이 올바르게 설정된다")
    void defaultValues() {
        Reminder reminder = Reminder.builder()
                .title("Simple task")
                .build();

        assertThat(reminder.getPriority()).isEqualTo(Priority.NONE);
        assertThat(reminder.isFlagged()).isFalse();
        assertThat(reminder.isCompleted()).isFalse();
        assertThat(reminder.getSortOrder()).isEqualTo(0);
        assertThat(reminder.getCreatedAt()).isNotNull();
        assertThat(reminder.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("update 호출 시 필드와 updatedAt이 갱신된다")
    void updateFields() throws InterruptedException {
        Reminder reminder = Reminder.builder()
                .title("Old title")
                .priority(Priority.NONE)
                .build();

        LocalDateTime originalUpdatedAt = reminder.getUpdatedAt();
        Thread.sleep(50);

        reminder.update("New title", "Some notes", "https://example.com",
                LocalDateTime.of(2026, 5, 1, 9, 0), Priority.HIGH, true);

        assertThat(reminder.getTitle()).isEqualTo("New title");
        assertThat(reminder.getNotes()).isEqualTo("Some notes");
        assertThat(reminder.getUrl()).isEqualTo("https://example.com");
        assertThat(reminder.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(reminder.isFlagged()).isTrue();
        assertThat(reminder.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("toggleComplete 호출 시 completed가 토글된다")
    void toggleComplete() {
        Reminder reminder = Reminder.builder()
                .title("Task")
                .build();

        assertThat(reminder.isCompleted()).isFalse();

        reminder.toggleComplete();
        assertThat(reminder.isCompleted()).isTrue();

        reminder.toggleComplete();
        assertThat(reminder.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("toggleComplete 호출 시 updatedAt이 갱신된다")
    void toggleCompleteUpdatesTimestamp() throws InterruptedException {
        Reminder reminder = Reminder.builder()
                .title("Task")
                .build();

        LocalDateTime originalUpdatedAt = reminder.getUpdatedAt();
        Thread.sleep(50);

        reminder.toggleComplete();

        assertThat(reminder.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("ReminderList에 연결할 수 있다")
    void associateWithList() {
        ReminderList list = ReminderList.builder()
                .name("Work")
                .color("#FF3B30")
                .build();

        Reminder reminder = Reminder.builder()
                .title("Task")
                .build();
        reminder.assignToList(list);

        assertThat(reminder.getReminderList()).isSameAs(list);
        assertThat(reminder.getReminderList().getName()).isEqualTo("Work");
    }
}
