package com.dinesh.TaskScheduler;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:4200") // CORS for Angular
public class TaskController {

    private final TaskRepo taskRepository;
    private final TaskStreamService taskStreamService;

    public TaskController(TaskRepo taskRepository, TaskStreamService taskStreamService) {
        this.taskRepository = taskRepository;
        this.taskStreamService = taskStreamService;
    }

    // Get all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Create task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        Task saved = taskRepository.save(task);
        taskStreamService.push(saved); // push to SSE
        return saved;
    }

    // Update task
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        Task updated = taskRepository.save(task);
        taskStreamService.push(updated); // push to SSE
        return updated;
    }

    // Delete task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.delete(task);
        task.setTitle("[Deleted] " + task.getTitle());
        taskStreamService.push(task); // optional notify
    }

    // SSE stream
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Task> streamTasks() {
    // 1. Send existing tasks first
    Flux<Task> existingTasks = Flux.fromIterable(taskRepository.findAll());
    // 2. Then push new tasks live
    return Flux.concat(existingTasks, taskStreamService.stream());
}

}
