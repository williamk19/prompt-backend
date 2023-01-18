package com.restful.promptbackend.Task;

import com.restful.promptbackend.User.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titleTask;

    @ManyToOne
    @JoinColumn(nullable=false)
    private User user;

    @Column(nullable = false)
    @NotNull
    private String descTask;

    @Column(nullable = false)
    @NotNull
    private String status;
    public Task(){}

    public Task(String titleTask, User user, String descTask, String status) {
        this.titleTask = titleTask;
        this.user = user;
        this.descTask = descTask;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleTask() {
        return titleTask;
    }

    public void setTitleTask(String titleTask) {
        this.titleTask = titleTask;
    }

    public String getDescTask() {
        return descTask;
    }

    public void setDescTask(String descTask) {
        this.descTask = descTask;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
