package com.javagda25.securitytemplate.repository;

import com.javagda25.securitytemplate.model.Task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
