package com.dinesh.demo;

import lombok.Data;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
