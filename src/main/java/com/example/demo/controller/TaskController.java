package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List; // For List

import com.example.demo.dto.Task;
import com.example.demo.dto.TaskRequest;

@RestController
@RequestMapping("/api")
public class TaskController {

    @PostMapping("/tasks")
    public ResponseEntity<String> receiveTasks(@RequestBody TaskRequest request) {
        String projectCode = request.getProjectCode();
        String memberId = request.getMemberId();
        List<Task> tasks = request.getTasks();

        // Process the received data (e.g., save to database)
        System.out.println("Project Code: " + projectCode);
        System.out.println("Member ID: " + memberId);
        System.out.println("Tasks: " + tasks);

        return ResponseEntity.ok("Tasks received successfully!");
    }
}
