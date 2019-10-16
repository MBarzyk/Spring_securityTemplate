package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Task.Task;
import com.javagda25.securitytemplate.model.Task.TaskStatus;
import com.javagda25.securitytemplate.model.account.Account;
import com.javagda25.securitytemplate.repository.AccountRepository;
import com.javagda25.securitytemplate.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final AccountRepository accountRepository;


    public void add(Task task, Account account) {
        task.setDateOfAddition(LocalDateTime.now());
        task.setTaskStatus(TaskStatus.TODO);
        task.setAccount(account);

        taskRepository.save(task);
    }

    public void setAsDone(Long taskId, String username) {
        if (!userIsOwnerOf(username, taskId)) {
            return;
        }

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent() && optionalTask.get().getTaskStatus() == TaskStatus.TODO) {
            Task task = optionalTask.get();
            task.setTaskStatus(TaskStatus.DONE);
            task.setDateOfFinish(LocalDateTime.now());

            taskRepository.save(task);
        }
    }

    public void setAsTodo(Long taskId, String username) {
        if (!userIsOwnerOf(username, taskId)) {
            return;
        }

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent() && optionalTask.get().getTaskStatus() == TaskStatus.DONE) {
            Task task = optionalTask.get();
            task.setTaskStatus(TaskStatus.TODO);
            task.setDateOfFinish(null);

            taskRepository.save(task);
        }
    }

    public void setAsArchive(Long taskId, String username) {
        if (!userIsOwnerOf(username, taskId)) {
            return;
        }

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent() && optionalTask.get().getTaskStatus() == TaskStatus.DONE) {
            Task task = optionalTask.get();
            task.setTaskStatus(TaskStatus.ARCHIVED);

            taskRepository.save(task);
        }
    }

    public Set<Task> getAll (String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            Account userAccount = account.get();
            return userAccount.getTasks();
        }
        return new HashSet<>();
    }

    public Set<Task> getAllCurrent(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            Account userAccount = account.get();
            return userAccount
                    .getTasks()
                    .stream()
                    .filter(task -> task.getTaskStatus() != TaskStatus.ARCHIVED)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public Set<Task> getAllArchived(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            Account userAccount = account.get();
            return userAccount
                    .getTasks()
                    .stream()
                    .filter(task -> task.getTaskStatus() == TaskStatus.ARCHIVED)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public boolean userIsOwnerOf(String username, Long taskId) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            Account user = account.get();

            return user.getTasks()
                    .stream()
                    .anyMatch(task -> task.getId() == taskId);
        }
        return false;
    }

    public void removeById(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(taskId);
        }
    }
}
