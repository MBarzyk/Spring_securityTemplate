package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Task.Task;
import com.javagda25.securitytemplate.model.account.Account;
import com.javagda25.securitytemplate.service.AccountService;
import com.javagda25.securitytemplate.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/task/")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AccountService accountService;

    @GetMapping(path = "/add")
    public String addTask (Model model, Task task) {

        model.addAttribute("task", task);

        return "task-add";
    }

    @PostMapping(path = "/add")
    public String addTask(Task task, Principal principal) {
        Optional<Account> optionalAccount = accountService.getByName(principal.getName());
        if (optionalAccount.isPresent()) {
            taskService.add(task, optionalAccount.get());
        }
        return "redirect:/task/list";
    }

    @GetMapping("/list")
    public String listBooks(Model model, Principal principal) {
        Optional<Account> optionalAccount = accountService.getByName(principal.getName());
        if (optionalAccount.isPresent()) {
            Set<Task> tasks = optionalAccount.get().getTasks();
            model.addAttribute("tasks", tasks);
            return "task-list";
        }

        return "redirect:/";
    }

    @GetMapping(path = "/done/{taskId}")
    public String setDone (@PathVariable(name = "taskId") Long taskId) {
        taskService.setAsDone(taskId);
        return "redirect:/task/list";
    }
    @GetMapping(path = "/todo/{taskId}")
    public String setTodo (@PathVariable(name = "taskId") Long taskId) {
        taskService.setAsTodo(taskId);
        return "redirect:/task/list";
    }
    @GetMapping(path = "/archive/{taskId}")
    public String setArchive (@PathVariable(name = "taskId") Long taskId) {
        taskService.setAsArchive(taskId);
        return "redirect:/task/list";
    }
}
