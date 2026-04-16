package zampano.ai.zampanoreminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zampano.ai.zampanoreminder.entity.Reminder;
import zampano.ai.zampanoreminder.repository.ReminderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    public Reminder findById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found: " + id));
    }

    @Transactional
    public Reminder create(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Transactional
    public void delete(Long id) {
        reminderRepository.deleteById(id);
    }

    public List<Reminder> findDueReminders() {
        return reminderRepository.findByCompletedFalseAndRemindAtBefore(LocalDateTime.now());
    }
}
