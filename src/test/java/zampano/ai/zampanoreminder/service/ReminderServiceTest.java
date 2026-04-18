package zampano.ai.zampanoreminder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.Priority;
import zampano.ai.zampanoreminder.domain.Reminder;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.dto.ReminderRequest;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;
import zampano.ai.zampanoreminder.repository.ReminderRepository;
import zampano.ai.zampanoreminder.service.ports.in.ReminderService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderListRepository reminderListRepository;

    private ReminderList testList;

    @BeforeEach
    void setUp() {
        testList = reminderListRepository.save(
                ReminderList.builder().name("Work").color("#FF3B30").build());
    }

    @Test
    @DisplayName("전체 리마인더를 조회할 수 있다")
    void findAll() {
        reminderRepository.save(Reminder.builder().title("Task 1").build());
        reminderRepository.save(Reminder.builder().title("Task 2").build());

        List<Reminder> result = reminderService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("ID로 리마인더를 조회할 수 있다")
    void findById() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("Task").priority(Priority.HIGH).build());

        Reminder result = reminderService.findById(saved.getId());

        assertThat(result.getTitle()).isEqualTo("Task");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 시 예외가 발생한다")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("새 리마인더를 생성할 수 있다")
    void create() {
        ReminderRequest request = ReminderRequest.builder()
                .title("New task")
                .notes("Some notes")
                .url("https://example.com")
                .remindAt(LocalDateTime.of(2026, 5, 1, 10, 0))
                .priority(Priority.HIGH)
                .flagged(true)
                .listId(testList.getId())
                .build();

        Reminder result = reminderService.create(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New task");
        assertThat(result.getNotes()).isEqualTo("Some notes");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.isFlagged()).isTrue();
        assertThat(result.getReminderList().getId()).isEqualTo(testList.getId());
    }

    @Test
    @DisplayName("리스트 없이 리마인더를 생성할 수 있다")
    void createWithoutList() {
        ReminderRequest request = ReminderRequest.builder()
                .title("No list task")
                .build();

        Reminder result = reminderService.create(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getReminderList()).isNull();
    }

    @Test
    @DisplayName("리마인더를 수정할 수 있다")
    void update() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("Old title").build());

        ReminderRequest request = ReminderRequest.builder()
                .title("New title")
                .notes("Updated notes")
                .priority(Priority.MEDIUM)
                .flagged(true)
                .build();

        Reminder result = reminderService.update(saved.getId(), request);

        assertThat(result.getTitle()).isEqualTo("New title");
        assertThat(result.getNotes()).isEqualTo("Updated notes");
        assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(result.isFlagged()).isTrue();
    }

    @Test
    @DisplayName("완료 상태를 토글할 수 있다")
    void toggleComplete() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("Task").build());

        assertThat(saved.isCompleted()).isFalse();

        Reminder toggled = reminderService.toggleComplete(saved.getId());
        assertThat(toggled.isCompleted()).isTrue();

        Reminder toggledBack = reminderService.toggleComplete(saved.getId());
        assertThat(toggledBack.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("리마인더를 삭제할 수 있다")
    void delete() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("To delete").build());
        Long savedId = saved.getId();

        reminderService.delete(savedId);

        assertThat(reminderRepository.findById(savedId)).isEmpty();
    }
}
