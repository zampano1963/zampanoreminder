package zampano.ai.zampanoreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.domain.Reminder;
import zampano.ai.zampanoreminder.domain.ReminderList;
import zampano.ai.zampanoreminder.dto.ReminderRequest;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;
import zampano.ai.zampanoreminder.repository.ReminderRepository;
import zampano.ai.zampanoreminder.service.ports.in.ReminderService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderService implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderListRepository reminderListRepository;

    @Override
    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    @Override
    public Reminder findById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found: " + id));
    }

    @Override
    @Transactional
    public Reminder create(ReminderRequest request) {
        Reminder reminder = Reminder.builder()
                .title(request.getTitle())
                .notes(request.getNotes())
                .url(request.getUrl())
                .remindAt(request.getRemindAt())
                .priority(request.getPriority())
                .flagged(request.getFlagged() != null && request.getFlagged())
                .build();

        if (request.getListId() != null) {
            ReminderList list = reminderListRepository.findById(request.getListId())
                    .orElseThrow(() -> new RuntimeException("ReminderList not found: " + request.getListId()));
            reminder.setReminderList(list);
        }

        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public Reminder update(Long id, ReminderRequest request) {
        Reminder reminder = findById(id);
        reminder.update(
                request.getTitle(),
                request.getNotes(),
                request.getUrl(),
                request.getRemindAt(),
                request.getPriority(),
                request.getFlagged() != null && request.getFlagged()
        );

        if (request.getListId() != null) {
            ReminderList list = reminderListRepository.findById(request.getListId())
                    .orElseThrow(() -> new RuntimeException("ReminderList not found: " + request.getListId()));
            reminder.setReminderList(list);
        }

        return reminder;
    }

    @Override
    @Transactional
    public Reminder toggleComplete(Long id) {
        Reminder reminder = findById(id);
        reminder.toggleComplete();
        return reminder;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = findById(id);
        reminderRepository.delete(reminder);
    }
}
