package com.restful.promptbackend.User;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RegistrationInfo {
  @NotNull
  @Length(min = 5, max = 30)
  private String username;

  @NotNull
  @Length(min = 8, max = 30)
  private String password;

  @Nullable
  @Length(max = 100)
  private String name;

  @Nullable
  @Length(max = 100)
  private String email;

  public RegistrationInfo(String username, String password, String name, String email) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
