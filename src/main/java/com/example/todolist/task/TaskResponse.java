package com.example.todolist.task;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskResponse {
    Long id;
    String title;
    String description;
    boolean completed;
    Instant createdAt;
    Instant updatedAt;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
