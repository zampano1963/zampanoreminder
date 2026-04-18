package zampano.ai.zampanoreminder.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderListRepository reminderListRepository;

    @Test
    @DisplayName("GET /api/lists - 전체 리스트를 조회한다")
    void findAll() throws Exception {
        reminderListRepository.save(ReminderList.builder().name("업무").color("#FF3B30").build());
        reminderListRepository.save(ReminderList.builder().name("개인").color("#007AFF").build());

        mockMvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].color").exists())
                .andExpect(jsonPath("$[0].reminderCount").exists());
    }

    @Test
    @DisplayName("GET /api/lists/{id} - ID로 리스트를 조회한다")
    void findById() throws Exception {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("업무").color("#FF3B30").icon("briefcase").build());

        mockMvc.perform(get("/api/lists/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("업무"))
                .andExpect(jsonPath("$.color").value("#FF3B30"))
                .andExpect(jsonPath("$.icon").value("briefcase"))
                .andExpect(jsonPath("$.deletable").value(true))
                .andExpect(jsonPath("$.reminderCount").value(0))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    @DisplayName("GET /api/lists/{id} - 존재하지 않는 ID 조회 시 500을 반환한다")
    void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/lists/{id}", 99))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("POST /api/lists - 새 리스트를 생성한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "업무",
                                    "color": "#FF3B30",
                                    "icon": "briefcase"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.color").value("#FF3B30"))
                .andExpect(jsonPath("$.icon").value("briefcase"))
                .andExpect(jsonPath("$.deletable").value(true))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @DisplayName("PUT /api/lists/{id} - 리스트를 수정한다")
    void update() throws Exception {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("업무").color("#FF3B30").icon("briefcase").build());

        mockMvc.perform(put("/api/lists/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "장보기",
                                    "color": "#34C759",
                                    "icon": "cart",
                                    "sortOrder": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("장보기"))
                .andExpect(jsonPath("$.color").value("#34C759"))
                .andExpect(jsonPath("$.icon").value("cart"))
                .andExpect(jsonPath("$.sortOrder").value(2));
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} - 리스트를 삭제한다")
    void deleteList() throws Exception {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("업무").color("#FF3B30").build());

        mockMvc.perform(delete("/api/lists/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/lists/{id}", saved.getId()))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} - 기본 리스트는 삭제할 수 없다")
    void deleteNonDeletableList() throws Exception {
        ReminderList saved = reminderListRepository.save(
                ReminderList.builder().name("미리알림").color("#007AFF").deletable(false).build());

        mockMvc.perform(delete("/api/lists/{id}", saved.getId()))
                .andExpect(status().is5xxServerError());
    }
}
