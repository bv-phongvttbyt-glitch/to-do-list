package com.example.todolist.task;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository repository;

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return repository.findAll().stream().map(TaskResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return repository.findById(id).map(TaskResponse::fromEntity)
                .orElseThrow(() -> new NoSuchElementException("Task %d not found".formatted(id)));
    }

    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        Task saved = repository.save(task);
        return TaskResponse.fromEntity(saved);
    }

    public TaskResponse update(Long id, TaskRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task %d not found".formatted(id)));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        return TaskResponse.fromEntity(task);
    }

    public TaskResponse toggleCompletion(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task %d not found".formatted(id)));
        task.setCompleted(!task.isCompleted());
        return TaskResponse.fromEntity(task);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Task %d not found".formatted(id));
        }
        repository.deleteById(id);
    }
}
