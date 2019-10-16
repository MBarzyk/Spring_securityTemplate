package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Task.Task;
import com.javagda25.securitytemplate.model.Task.TaskStatus;
import com.javagda25.securitytemplate.model.account.Account;
import com.javagda25.securitytemplate.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;


    public void add(Task task, Account account) {
        task.setDateOfAddition(LocalDateTime.now());
        task.setTaskStatus(TaskStatus.TODO);
        task.setAccount(account);

        taskRepository.save(task);
    }

    public void setAsDone(Long taskId) {
        setStatus(taskId, TaskStatus.DONE, LocalDateTime.now());
    }
    public void setAsTodo(Long taskId) {
        setStatus(taskId, TaskStatus.TODO, null);
    }

    public void setAsArchive(Long taskId) {
        setStatus(taskId, TaskStatus.ARCHIVED, LocalDateTime.now());
    }

    private void setStatus(Long taskId, TaskStatus todo, LocalDateTime date) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setDateOfFinish(date);
            task.setTaskStatus(todo);
            taskRepository.save(task);
        }
    }
}
