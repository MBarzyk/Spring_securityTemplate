package com.javagda25.securitytemplate.model.Task;

import com.javagda25.securitytemplate.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String content;

    @Column(nullable = false)
    private LocalDateTime dateOfAddition;

    private LocalDateTime dateOfFinish;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    public boolean isArchived () {
        return taskStatus == TaskStatus.ARCHIVED;
    }
    public boolean isTodo () {
        return taskStatus == TaskStatus.TODO;
    }
    public boolean isDone () {
        return taskStatus == TaskStatus.DONE;
    }
}
