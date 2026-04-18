package zampano.ai.zampanoreminder.service.ports.in;

import zampano.ai.zampanoreminder.domain.ReminderList;

import java.util.List;

public interface ReminderListService {

    List<ReminderList> findAll();

    ReminderList findById(Long id);

    ReminderList create(String name, String color, String icon);

    ReminderList update(Long id, String name, String color, String icon, int sortOrder);

    void delete(Long id);
}
