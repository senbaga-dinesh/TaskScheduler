package com.dinesh.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TaskExecutor {

    private final RestTemplate restTemplate;

    // Inject the LoadBalanced RestTemplate from configuration
    public TaskExecutor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Runs every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void executeTasks() {
        try {
            // Use Eureka service name (must match spring.application.name in TaskScheduler)
            String taskServiceUrl = "http://taskscheduler:8081/tasks"; 
            
            // Fetch tasks from TaskScheduler
            Task[] tasks = restTemplate.getForObject(taskServiceUrl, Task[].class);

            if (tasks != null) {
                for (Task task : tasks) {
                    if (!task.isCompleted()) {
                        System.out.println("Executing task: " + task.getTitle());

                        // Mark task as completed in TaskScheduler
                        task.setCompleted(true);
                        restTemplate.put(taskServiceUrl + "/" + task.getId(), task);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch or update tasks: " + e.getMessage());
        }
    }
}
