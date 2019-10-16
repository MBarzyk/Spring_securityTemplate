package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Task.Task;
import com.javagda25.securitytemplate.model.account.Account;
import com.javagda25.securitytemplate.service.AccountService;
import com.javagda25.securitytemplate.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(path = "/remove/{taskId}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public String removeTask (@PathVariable(name = "taskId") Long taskId, HttpServletRequest request) {
        taskService.removeById(taskId);
        String referer = request.getHeader("referer");

        return "redirect:"+referer;
    }

    @PostMapping(path = "/add")
    public String addTask(Task task, Principal principal) {
        Optional<Account> optionalAccount = accountService.getByName(principal.getName());
        if (optionalAccount.isPresent()) {
            taskService.add(task, optionalAccount.get());
        }
        return "redirect:/task/list";
    }

    @GetMapping("/list/current")
    public String listCurrent (Model model, Principal principal) {
        Set<Task> taskSet = taskService.getAllCurrent(principal.getName());
        model.addAttribute("tasks", taskSet);
        return "task-list";
    }
    @GetMapping("/list/archived")
    public String listArchived (Model model, Principal principal) {
        Set<Task> taskSet = taskService.getAllArchived(principal.getName());
        model.addAttribute("tasks", taskSet);
        return "task-list";
    }

    @GetMapping(path = "/done/{taskId}")
    public String setDone (@PathVariable(name = "taskId") Long taskId, Principal principal) {
        taskService.setAsDone(taskId, principal.getName());
        return "redirect:/task/list";
    }
    @GetMapping(path = "/todo/{taskId}")
    public String setTodo (@PathVariable(name = "taskId") Long taskId, Principal principal) {
        taskService.setAsTodo(taskId, principal.getName());
        return "redirect:/task/list";
    }
    @GetMapping(path = "/archive/{taskId}")
    public String setArchive (@PathVariable(name = "taskId") Long taskId, Principal principal) {
        taskService.setAsArchive(taskId, principal.getName());
        return "redirect:/task/list";
    }
}
