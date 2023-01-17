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
    @Column(nullable = false, length = 50)
    private String users;

    @Column(nullable = false)
    @NotNull
    private String descTask;

    @Column(nullable = false)
    @NotNull
    private String status;
    public Task(){}

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
    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
