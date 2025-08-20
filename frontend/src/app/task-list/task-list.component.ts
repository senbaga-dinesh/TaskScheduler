import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";

interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
}

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container">
      <h1>Task List (Real-time)</h1>
      <div class="tasks">
        <div class="task-card" *ngFor="let task of tasks">
          <h3>{{ task.title }}</h3>
          <p>{{ task.description }}</p>
          <p>Status: <span [class.completed]="task.completed">{{ task.completed ? 'Done ✅' : 'Pending ⏳' }}</span></p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .tasks {
      display: flex;
      flex-direction: column;
      gap: 10px;
    }
    .task-card {
      padding: 10px;
      border-left: 5px solid #00796b;
      background-color: #e0f7fa;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .completed {
      color: green;
      font-weight: bold;
    }
  `]
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];

  ngOnInit() {
    // 1️⃣ Fetch existing tasks first
    fetch('http://localhost:8081/tasks')
      .then(res => res.json())
      .then((data: Task[]) => this.tasks = data.reverse())
      .catch(err => console.error('Error fetching tasks', err));

    // 2️⃣ Subscribe to SSE for real-time updates
    const eventSource = new EventSource('http://localhost:8081/tasks/stream');

    eventSource.onmessage = (event) => {
      const task: Task = JSON.parse(event.data);
      const index = this.tasks.findIndex(t => t.id === task.id);
      if (index !== -1) {
        this.tasks[index] = task; // update
      } else {
        this.tasks.unshift(task); // new task
      }
    };

    eventSource.onerror = (err) => {
      console.error('SSE connection error', err);
      eventSource.close();
    };
  }
}
