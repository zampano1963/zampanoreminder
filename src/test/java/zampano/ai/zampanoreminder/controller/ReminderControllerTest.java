package zampano.ai.zampanoreminder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.Priority;
import zampano.ai.zampanoreminder.domain.Reminder;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;
import zampano.ai.zampanoreminder.repository.ReminderRepository;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("GET /api/reminders - 전체 리마인더를 조회한다")
    void findAll() throws Exception {
        reminderRepository.save(Reminder.builder().title("Task 1").reminderList(testList).build());
        reminderRepository.save(Reminder.builder().title("Task 2").reminderList(testList).build());

        mockMvc.perform(get("/api/reminders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").exists())
                .andExpect(jsonPath("$[0].priority").exists())
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    @DisplayName("GET /api/reminders/{id} - ID로 리마인더를 조회한다")
    void findById() throws Exception {
        Reminder saved = reminderRepository.save(Reminder.builder()
                .title("Task")
                .notes("Some notes")
                .priority(Priority.HIGH)
                .flagged(true)
                .reminderList(testList)
                .build());

        mockMvc.perform(get("/api/reminders/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.title").value("Task"))
                .andExpect(jsonPath("$.notes").value("Some notes"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.flagged").value(true))
                .andExpect(jsonPath("$.listId").value(testList.getId()));
    }

    @Test
    @DisplayName("POST /api/reminders - 새 리마인더를 생성한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "New task",
                                    "notes": "Details",
                                    "priority": "HIGH",
                                    "flagged": true,
                                    "listId": %d
                                }
                                """.formatted(testList.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.flagged").value(true))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.listId").value(testList.getId()));
    }

    @Test
    @DisplayName("PUT /api/reminders/{id} - 리마인더를 수정한다")
    void update() throws Exception {
        Reminder saved = reminderRepository.save(Reminder.builder()
                .title("Old title")
                .priority(Priority.NONE)
                .reminderList(testList)
                .build());

        mockMvc.perform(put("/api/reminders/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Updated title",
                                    "notes": "Updated notes",
                                    "priority": "MEDIUM",
                                    "flagged": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.notes").value("Updated notes"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.flagged").value(true));
    }

    @Test
    @DisplayName("PATCH /api/reminders/{id}/toggle - 완료 상태를 토글한다")
    void toggleComplete() throws Exception {
        Reminder saved = reminderRepository.save(Reminder.builder()
                .title("Task")
                .build());

        mockMvc.perform(patch("/api/reminders/{id}/toggle", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        mockMvc.perform(patch("/api/reminders/{id}/toggle", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("DELETE /api/reminders/{id} - 리마인더를 삭제한다")
    void deleteReminder() throws Exception {
        Reminder saved = reminderRepository.save(Reminder.builder()
                .title("To delete")
                .build());

        mockMvc.perform(delete("/api/reminders/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/reminders/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }
}
