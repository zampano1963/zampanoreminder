package zampano.ai.zampanoreminder.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zampano.ai.zampanoreminder.dto.ReminderListRequest;
import zampano.ai.zampanoreminder.dto.ReminderListResponse;
import zampano.ai.zampanoreminder.repository.ReminderListRepository;
import zampano.ai.zampanoreminder.service.ports.in.ReminderListService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ReminderListController {

    private final ReminderListService reminderListService;
    private final ReminderListRepository reminderListRepository;

    @GetMapping
    public List<ReminderListResponse> findAll() {
        Map<Long, Long> countMap = reminderListRepository.countRemindersPerList().stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        return reminderListService.findAll().stream()
                .map(list -> ReminderListResponse.from(list, countMap.getOrDefault(list.getId(), 0L).intValue()))
                .toList();
    }

    @GetMapping("/{id}")
    public ReminderListResponse findById(@PathVariable Long id) {
        var list = reminderListService.findById(id);
        Map<Long, Long> countMap = reminderListRepository.countRemindersPerList().stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
        return ReminderListResponse.from(list, countMap.getOrDefault(list.getId(), 0L).intValue());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderListResponse create(@Valid @RequestBody ReminderListRequest request) {
        return ReminderListResponse.from(
                reminderListService.create(request.getName(), request.getColor(), request.getIcon()), 0);
    }

    @PutMapping("/{id}")
    public ReminderListResponse update(@PathVariable Long id, @Valid @RequestBody ReminderListRequest request) {
        return ReminderListResponse.from(
                reminderListService.update(id, request.getName(), request.getColor(), request.getIcon(), request.getSortOrder()), 0);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderListService.delete(id);
    }
}
