package zampano.ai.zampanoreminder.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import zampano.ai.zampanoreminder.domain.Reminder;
import zampano.ai.zampanoreminder.repository.ReminderRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final ReminderRepository reminderRepository;

    @Override
    public void run(ApplicationArguments args) {
        Reminder sample = Reminder.builder()
                .title("Test Reminder")
                .description("Verify JPA + H2 works")
                .remindAt(LocalDateTime.now().plusHours(1))
                .completed(false)
                .build();
        reminderRepository.save(sample);
        log.info("Sample reminder created: {}", sample.getId());
    }
}
