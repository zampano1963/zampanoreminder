package zampano.ai.zampanoreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zampano.ai.zampanoreminder.domain.Reminder;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByCompletedFalseAndRemindAtBefore(LocalDateTime dateTime);
}
