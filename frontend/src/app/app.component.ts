import { Component } from '@angular/core';
import { TaskListComponent } from './task-list/task-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [TaskListComponent],
  template: `<h1>Task Dashboard</h1><app-task-list></app-task-list>`
})
export class AppComponent {}
