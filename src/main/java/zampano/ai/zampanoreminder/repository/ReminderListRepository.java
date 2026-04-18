package zampano.ai.zampanoreminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zampano.ai.zampanoreminder.domain.ReminderList;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {
}
