package zampano.ai.zampanoreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zampano.ai.zampanoreminder.domain.ReminderList;

import java.util.Map;
import java.util.List;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {

    @Query("SELECT r.reminderList.id, COUNT(r) FROM Reminder r WHERE r.reminderList IS NOT NULL GROUP BY r.reminderList.id")
    List<Object[]> countRemindersPerList();
}
