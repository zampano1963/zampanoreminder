package zampano.ai.zampanoreminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zampano.ai.zampanoreminder.entity.Reminder;
import zampano.ai.zampanoreminder.service.ReminderService;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping
    public List<Reminder> findAll() {
        return reminderService.findAll();
    }

    @GetMapping("/{id}")
    public Reminder findById(@PathVariable Long id) {
        return reminderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reminder create(@RequestBody Reminder reminder) {
        return reminderService.create(reminder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderService.delete(id);
    }
}
