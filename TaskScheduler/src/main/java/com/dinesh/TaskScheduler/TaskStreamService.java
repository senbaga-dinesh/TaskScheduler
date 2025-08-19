package com.dinesh.TaskScheduler;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class TaskStreamService {

    private final Sinks.Many<Task> sink;

    public TaskStreamService() {
        // Multicast for live subscribers
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void push(Task task) {
        Sinks.EmitResult result = sink.tryEmitNext(task);
        if (result.isFailure()) {
            System.out.println("Failed to push SSE: " + result);
        }
    }

    public Flux<Task> stream() {
        return sink.asFlux();
    }
}


