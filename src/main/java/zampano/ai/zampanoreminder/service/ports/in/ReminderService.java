package zampano.ai.zampanoreminder.service.ports.in;

import zampano.ai.zampanoreminder.domain.Reminder;
import zampano.ai.zampanoreminder.dto.ReminderRequest;

import java.util.List;

public interface ReminderService {

    List<Reminder> findAll();

    Reminder findById(Long id);

    Reminder create(ReminderRequest request);

    Reminder update(Long id, ReminderRequest request);

    Reminder toggleComplete(Long id);

    void delete(Long id);
}
