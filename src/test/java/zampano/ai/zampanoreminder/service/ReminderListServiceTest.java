package zampano.ai.zampanoreminder.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ReminderListServiceTest {

    @Mock
    private ReminderListRepository reminderListRepository;

    @InjectMocks
    private ReminderListService reminderListService;

    @Test
    @DisplayName("전체 리스트를 조회할 수 있다")
    void findAll() {
        List<ReminderList> lists = List.of(
                ReminderList.builder().name("업무").color("#FF3B30").build(),
                ReminderList.builder().name("개인").color("#007AFF").build()
        );
        given(reminderListRepository.findAll()).willReturn(lists);

        List<ReminderList> result = reminderListService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("업무");
        assertThat(result.get(1).getName()).isEqualTo("개인");
    }

    @Test
    @DisplayName("ID로 리스트를 조회할 수 있다")
    void findById() {
        ReminderList list = ReminderList.builder().name("업무").color("#FF3B30").build();
        given(reminderListRepository.findById(1L)).willReturn(Optional.of(list));

        ReminderList result = reminderListService.findById(1L);

        assertThat(result.getName()).isEqualTo("업무");
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 시 예외가 발생한다")
    void findByIdNotFound() {
        given(reminderListRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reminderListService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("새 리스트를 생성할 수 있다")
    void create() {
        given(reminderListRepository.save(any(ReminderList.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ReminderList result = reminderListService.create("업무", "#FF3B30", "briefcase");

        assertThat(result.getName()).isEqualTo("업무");
        assertThat(result.getColor()).isEqualTo("#FF3B30");
        assertThat(result.getIcon()).isEqualTo("briefcase");
        assertThat(result.isDeletable()).isTrue();
        then(reminderListRepository).should().save(any(ReminderList.class));
    }

    @Test
    @DisplayName("리스트를 수정할 수 있다")
    void update() {
        ReminderList list = ReminderList.builder().name("업무").color("#FF3B30").icon("briefcase").build();
        given(reminderListRepository.findById(1L)).willReturn(Optional.of(list));

        ReminderList result = reminderListService.update(1L, "장보기", "#34C759", "cart", 2);

        assertThat(result.getName()).isEqualTo("장보기");
        assertThat(result.getColor()).isEqualTo("#34C759");
        assertThat(result.getIcon()).isEqualTo("cart");
        assertThat(result.getSortOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("deletable=true인 리스트를 삭제할 수 있다")
    void deleteDeletableList() {
        ReminderList list = ReminderList.builder().name("업무").color("#FF3B30").deletable(true).build();
        given(reminderListRepository.findById(1L)).willReturn(Optional.of(list));

        reminderListService.delete(1L);

        then(reminderListRepository).should().delete(list);
    }

    @Test
    @DisplayName("deletable=false인 기본 리스트는 삭제할 수 없다")
    void deleteNonDeletableListThrows() {
        ReminderList list = ReminderList.builder().name("미리알림").color("#007AFF").deletable(false).build();
        given(reminderListRepository.findById(1L)).willReturn(Optional.of(list));

        assertThatThrownBy(() -> reminderListService.delete(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("기본 리스트는 삭제할 수 없습니다");

        then(reminderListRepository).shouldHaveNoMoreInteractions();
    }
}
