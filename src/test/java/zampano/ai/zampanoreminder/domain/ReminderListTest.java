package zampano.ai.zampanoreminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderListTest {

    @Test
    @DisplayName("Builder로 ReminderList를 생성할 수 있다")
    void createWithBuilder() {
        ReminderList list = ReminderList.builder()
                .name("업무")
                .color("#FF3B30")
                .icon("briefcase")
                .build();

        assertThat(list.getName()).isEqualTo("업무");
        assertThat(list.getColor()).isEqualTo("#FF3B30");
        assertThat(list.getIcon()).isEqualTo("briefcase");
    }

    @Test
    @DisplayName("기본값이 올바르게 설정된다 (sortOrder=0, deletable=true)")
    void defaultValues() {
        ReminderList list = ReminderList.builder()
                .name("기본 리스트")
                .color("#007AFF")
                .build();

        assertThat(list.getSortOrder()).isEqualTo(0);
        assertThat(list.isDeletable()).isTrue();
        assertThat(list.getReminders()).isEmpty();
    }

    @Test
    @DisplayName("deletable=false로 생성할 수 있다")
    void createNonDeletable() {
        ReminderList list = ReminderList.builder()
                .name("미리알림")
                .color("#007AFF")
                .deletable(false)
                .build();

        assertThat(list.isDeletable()).isFalse();
    }

    @Test
    @DisplayName("생성 시 createdAt과 updatedAt이 자동 설정된다")
    void createdAtAndUpdatedAtAreSetOnCreate() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ReminderList list = ReminderList.builder()
                .name("업무")
                .color("#FF3B30")
                .build();

        assertThat(list.getCreatedAt()).isNotNull();
        assertThat(list.getUpdatedAt()).isNotNull();
        assertThat(list.getCreatedAt()).isAfter(before);
        assertThat(list.getUpdatedAt()).isAfter(before);
    }

    @Test
    @DisplayName("update 호출 시 updatedAt이 갱신되고 createdAt은 변경되지 않는다")
    void updateChangesUpdatedAtButNotCreatedAt() throws InterruptedException {
        ReminderList list = ReminderList.builder()
                .name("업무")
                .color("#FF3B30")
                .build();

        LocalDateTime originalCreatedAt = list.getCreatedAt();
        LocalDateTime originalUpdatedAt = list.getUpdatedAt();

        Thread.sleep(50);

        list.update("업무 (수정)", "#FF9500", "star", 2);

        assertThat(list.getName()).isEqualTo("업무 (수정)");
        assertThat(list.getColor()).isEqualTo("#FF9500");
        assertThat(list.getIcon()).isEqualTo("star");
        assertThat(list.getSortOrder()).isEqualTo(2);
        assertThat(list.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(list.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("update 메서드로 이름, 색상, 아이콘, 순서를 변경할 수 있다")
    void updateAllFields() {
        ReminderList list = ReminderList.builder()
                .name("쇼핑")
                .color("#34C759")
                .icon("cart")
                .sortOrder(1)
                .build();

        list.update("장보기", "#AF52DE", "bag", 3);

        assertThat(list.getName()).isEqualTo("장보기");
        assertThat(list.getColor()).isEqualTo("#AF52DE");
        assertThat(list.getIcon()).isEqualTo("bag");
        assertThat(list.getSortOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("Reminder를 리스트에 추가할 수 있다")
    void addReminderToList() {
        ReminderList list = ReminderList.builder()
                .name("업무")
                .color("#FF3B30")
                .build();

        Reminder reminder = Reminder.builder()
                .title("회의 참석")
                .remindAt(LocalDateTime.now().plusHours(1))
                .build();
        reminder.setReminderList(list);
        list.getReminders().add(reminder);

        assertThat(list.getReminders()).hasSize(1);
        assertThat(list.getReminders().get(0).getTitle()).isEqualTo("회의 참석");
        assertThat(list.getReminders().get(0).getReminderList()).isSameAs(list);
    }
}
