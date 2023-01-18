package com.restful.promptbackend.Task;

import javax.validation.constraints.NotNull;

public class TaskRequest {
  @NotNull
  private String titleTask;

  @NotNull
  private String descTask;

  @NotNull
  private String status;

  @NotNull
  private Integer userId;

  public TaskRequest(String titleTask, String descTask, String status, Integer userId) {
    this.titleTask = titleTask;
    this.descTask = descTask;
    this.status = status;
    this.userId = userId;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer user) {
    this.userId = user;
  }
}
