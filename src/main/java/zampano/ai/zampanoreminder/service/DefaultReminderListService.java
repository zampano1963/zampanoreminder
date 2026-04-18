package zampano.ai.zampanoreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.exception.BusinessRuleException;
import zampano.ai.zampanoreminder.exception.EntityNotFoundException;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;
import zampano.ai.zampanoreminder.service.ports.in.ReminderListService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderListService implements ReminderListService {

    private final ReminderListRepository reminderListRepository;

    @Override
    public List<ReminderList> findAll() {
        return reminderListRepository.findAll();
    }

    @Override
    public ReminderList findById(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ReminderList", id));
    }

    @Override
    @Transactional
    public ReminderList create(String name, String color, String icon) {
        ReminderList list = ReminderList.builder()
                .name(name)
                .color(color)
                .icon(icon)
                .build();
        return reminderListRepository.save(list);
    }

    @Override
    @Transactional
    public ReminderList update(Long id, String name, String color, String icon, int sortOrder) {
        ReminderList list = findById(id);
        list.update(name, color, icon, sortOrder);
        return list;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReminderList list = findById(id);
        if (!list.isDeletable()) {
            throw new BusinessRuleException("Default list cannot be deleted: " + id);
        }
        reminderListRepository.delete(list);
    }
}
