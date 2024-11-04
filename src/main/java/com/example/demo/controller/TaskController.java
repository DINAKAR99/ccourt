package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List; // For List

import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DateRangeRequest;
import com.example.demo.dto.Task;
import com.example.demo.dto.TaskRequest;
import com.example.demo.services.TaskService;

@RestController
@RequestMapping("/public/api")
public class TaskController {

    
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<String> receiveTasks(@RequestBody TaskRequest request) {
        List<Task> tasks = request.getTasks();

        // Save tasks to the database
        taskService.saveAllTasks(tasks);

        return ResponseEntity.ok("Tasks received and saved successfully!");
    }
  @PostMapping("/tasks/today")
public ResponseEntity<List<Task>> getTodayTasksForLoggedInUser() {
    // Retrieve the authenticated user's details from the SecurityContext
    // Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

    // if (authentication == null || !authentication.isAuthenticated()) {
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
    // }

    // String memberId = authentication.getName(); // Assuming memberId is stored as the principal name

    // Get today's date
    LocalDate today = LocalDate.now();

    // Fetch tasks for the logged-in user
    List<Task> tasks = taskService.getTasksForToday("M001");

    return ResponseEntity.ok(tasks);
}
@PostMapping("/tasks/range")
public ResponseEntity<List<Task>> getTasksByDateRange(@RequestBody DateRangeRequest request) {
    LocalDate fromDate = request.getFromDate();
    LocalDate toDate = request.getToDate();
    // String memberId = SecurityContextHolder.getContext().getAuthentication().getName(); // Assuming you have member ID from JWT

    List<Task> tasks = taskService.findTasksByDateRange("M001", fromDate, toDate);
    
    return ResponseEntity.ok(tasks);
}

}

