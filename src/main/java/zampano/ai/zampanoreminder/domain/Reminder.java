package zampano.ai.zampanoreminder.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String notes;

    private String url;

    private LocalDateTime remindAt;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NONE;

    @Column(nullable = false)
    @Builder.Default
    private boolean flagged = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean completed = false;

    @Column(nullable = false)
    @Builder.Default
    private int sortOrder = 0;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private ReminderList reminderList;

    public void update(String title, String notes, String url, LocalDateTime remindAt, Priority priority, boolean flagged) {
        this.title = title;
        this.notes = notes;
        this.url = url;
        this.remindAt = remindAt;
        this.priority = priority;
        this.flagged = flagged;
        this.updatedAt = LocalDateTime.now();
    }

    public void toggleComplete() {
        this.completed = !this.completed;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignToList(ReminderList list) {
        this.reminderList = list;
        this.updatedAt = LocalDateTime.now();
    }
}
