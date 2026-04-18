package zampano.ai.zampanoreminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zampano.ai.zampanoreminder.dto.ReminderRequest;
import zampano.ai.zampanoreminder.dto.ReminderResponse;
import zampano.ai.zampanoreminder.service.ports.in.ReminderService;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping
    public List<ReminderResponse> findAll() {
        return reminderService.findAll().stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ReminderResponse findById(@PathVariable Long id) {
        return ReminderResponse.from(reminderService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@RequestBody ReminderRequest request) {
        return ReminderResponse.from(reminderService.create(request));
    }

    @PutMapping("/{id}")
    public ReminderResponse update(@PathVariable Long id, @RequestBody ReminderRequest request) {
        return ReminderResponse.from(reminderService.update(id, request));
    }

    @PatchMapping("/{id}/toggle")
    public ReminderResponse toggleComplete(@PathVariable Long id) {
        return ReminderResponse.from(reminderService.toggleComplete(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderService.delete(id);
    }
}
