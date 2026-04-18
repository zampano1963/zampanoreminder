package zampano.ai.zampanoreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderListService {

    private final ReminderListRepository reminderListRepository;

    public List<ReminderList> findAll() {
        return reminderListRepository.findAll();
    }

    public ReminderList findById(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReminderList not found: " + id));
    }

    @Transactional
    public ReminderList create(String name, String color, String icon) {
        ReminderList list = ReminderList.builder()
                .name(name)
                .color(color)
                .icon(icon)
                .build();
        return reminderListRepository.save(list);
    }

    @Transactional
    public ReminderList update(Long id, String name, String color, String icon, int sortOrder) {
        ReminderList list = findById(id);
        list.update(name, color, icon, sortOrder);
        return list;
    }

    @Transactional
    public void delete(Long id) {
        ReminderList list = findById(id);
        if (!list.isDeletable()) {
            throw new RuntimeException("기본 리스트는 삭제할 수 없습니다: " + id);
        }
        reminderListRepository.delete(list);
    }
}
