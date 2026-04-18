package zampano.ai.zampanoreminder.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import zampano.ai.zampanoreminder.domain.Priority;
import zampano.ai.zampanoreminder.domain.Reminder;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;
import zampano.ai.zampanoreminder.repository.ReminderRepository;

import java.time.LocalDateTime;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final ReminderRepository reminderRepository;
    private final ReminderListRepository reminderListRepository;

    @Override
    public void run(ApplicationArguments args) {
        ReminderList defaultList = reminderListRepository.save(
                ReminderList.builder().name("Reminders").color("#007AFF").icon("list.bullet").deletable(false).build());
        ReminderList workList = reminderListRepository.save(
                ReminderList.builder().name("Work").color("#FF3B30").icon("briefcase").sortOrder(1).build());
        ReminderList personalList = reminderListRepository.save(
                ReminderList.builder().name("Personal").color("#34C759").icon("person").sortOrder(2).build());

        reminderRepository.save(Reminder.builder()
                .title("Buy groceries")
                .notes("Milk, eggs, bread")
                .remindAt(LocalDateTime.now().plusHours(2))
                .priority(Priority.MEDIUM)
                .reminderList(defaultList)
                .build());

        reminderRepository.save(Reminder.builder()
                .title("Team meeting")
                .notes("Discuss Q2 roadmap")
                .url("https://meet.google.com/abc")
                .remindAt(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                .priority(Priority.HIGH)
                .flagged(true)
                .reminderList(workList)
                .build());

        reminderRepository.save(Reminder.builder()
                .title("Read a book")
                .remindAt(LocalDateTime.now().plusDays(2))
                .priority(Priority.LOW)
                .reminderList(personalList)
                .build());

        reminderRepository.save(Reminder.builder()
                .title("Pay electricity bill")
                .notes("Due by end of month")
                .remindAt(LocalDateTime.now().plusDays(5))
                .priority(Priority.HIGH)
                .flagged(true)
                .reminderList(defaultList)
                .build());

        reminderRepository.save(Reminder.builder()
                .title("Call dentist")
                .remindAt(LocalDateTime.now().plusHours(4))
                .reminderList(personalList)
                .build());

        reminderRepository.save(Reminder.builder()
                .title("Submit report")
                .notes("Monthly performance report")
                .remindAt(LocalDateTime.now().minusHours(1))
                .priority(Priority.HIGH)
                .completed(true)
                .reminderList(workList)
                .build());

        log.info("Sample data initialized: 3 lists, 6 reminders");
    }
}
