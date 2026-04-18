package zampano.ai.zampanoreminder.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.service.ports.in.ReminderListService;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderListServiceTest {

    @Autowired
    private ReminderListService reminderListService;

    @Autowired
    private ReminderListRepository reminderListRepository;

    @Test
    @DisplayName("전체 리스트를 조회할 수 있다")
    void findAll() {
        reminderListRepository.save(ReminderList.builder().name("업무").color("#FF3B30").build());
        reminderListRepository.save(ReminderList.builder().name("개인").color("#007AFF").build());

        List<ReminderList> result = reminderListService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ReminderList::getName)
                .containsExactlyInAnyOrder("업무", "개인");
    }

    @Test
    @DisplayName("ID로 리스트를 조회할 수 있다")
    void findById() {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("업무").color("#FF3B30").build());

        ReminderList result = reminderListService.findById(saved.getId());

        assertThat(result.getName()).isEqualTo("업무");
        assertThat(result.getColor()).isEqualTo("#FF3B30");
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 시 예외가 발생한다")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderListService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("새 리스트를 생성할 수 있다")
    void create() {
        ReminderList result = reminderListService.create("업무", "#FF3B30", "briefcase");

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("업무");
        assertThat(result.getColor()).isEqualTo("#FF3B30");
        assertThat(result.getIcon()).isEqualTo("briefcase");
        assertThat(result.isDeletable()).isTrue();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("리스트를 수정할 수 있다")
    void update() {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("업무").color("#FF3B30").icon("briefcase").build());

        ReminderList result = reminderListService.update(saved.getId(), "장보기", "#34C759", "cart", 2);

        assertThat(result.getName()).isEqualTo("장보기");
        assertThat(result.getColor()).isEqualTo("#34C759");
        assertThat(result.getIcon()).isEqualTo("cart");
        assertThat(result.getSortOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("deletable=true인 리스트를 삭제할 수 있다")
    void deleteDeletableList() {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("업무").color("#FF3B30").deletable(true).build());
        Long savedId = saved.getId();

        reminderListService.delete(savedId);

        assertThat(reminderListRepository.findById(savedId)).isEmpty();
    }

    @Test
    @DisplayName("deletable=false인 기본 리스트는 삭제할 수 없다")
    void deleteNonDeletableListThrows() {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("미리알림").color("#007AFF").deletable(false).build());

        assertThatThrownBy(() -> reminderListService.delete(saved.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("기본 리스트는 삭제할 수 없습니다");

        assertThat(reminderListRepository.findById(saved.getId())).isPresent();
    }
}
