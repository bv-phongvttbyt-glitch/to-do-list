package com.example.todolist.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void create_and_fetch_task() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Viết tài liệu");
        request.setDescription("Chuẩn bị README");

        TaskResponse created = service.create(request);

        TaskResponse fetched = service.findById(created.getId());
        assertThat(fetched.getTitle()).isEqualTo("Viết tài liệu");
        assertThat(fetched.getDescription()).isEqualTo("Chuẩn bị README");
        assertThat(fetched.isCompleted()).isFalse();
    }

    @Test
    void update_task_changes_fields() {
        Task task = new Task();
        task.setTitle("Old");
        task = repository.save(task);

        TaskRequest request = new TaskRequest();
        request.setTitle("New");
        request.setDescription("Mô tả mới");
        request.setCompleted(true);

        TaskResponse updated = service.update(task.getId(), request);
        assertThat(updated.getTitle()).isEqualTo("New");
        assertThat(updated.getDescription()).isEqualTo("Mô tả mới");
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    void toggle_changes_completion_state() {
        Task task = new Task();
        task.setTitle("Toggle me");
        task = repository.save(task);

        TaskResponse toggled = service.toggleCompletion(task.getId());
        assertThat(toggled.isCompleted()).isTrue();

        TaskResponse toggledBack = service.toggleCompletion(task.getId());
        assertThat(toggledBack.isCompleted()).isFalse();
    }

    @Test
    void delete_throws_when_missing() {
        assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessageContaining("999");
    }

    @Test
    void find_all_returns_list() {
        Task first = new Task();
        first.setTitle("A");
        repository.save(first);

        Task second = new Task();
        second.setTitle("B");
        repository.save(second);

        List<TaskResponse> tasks = service.findAll();
        assertThat(tasks).hasSize(2);
    }
}
