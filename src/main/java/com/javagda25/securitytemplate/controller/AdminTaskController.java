package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Task.Task;
import com.javagda25.securitytemplate.service.AccountService;
import com.javagda25.securitytemplate.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping(path = "/admin/task/")
@PreAuthorize(value = "hasRole('ADMIN')")
public class AdminTaskController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/list")
    public String getTaskListForm(Model model) {
        model.addAttribute("userlist", accountService.getAll());

        return "user-chooser";
    }

    @PostMapping("/list")
    public String getTasksOfUser(Model model, String chosenUsername) {
        Set<Task> taskSet = taskService.getAll(chosenUsername);

        model.addAttribute("tasks", taskSet);

        return "task-list";
    }
}