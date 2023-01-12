package com.restful.promptbackend.User;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RegistrationInfo {
  @NotNull
  @Length(min = 5, max = 16)
  @Pattern(regexp = "^[a-zA-Z0-9]+$")
  private String username;

  @NotNull
  @Length(min = 8, max = 16)
  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[!@#_])(?=.*[A-Z]).*$")
  private String password;

  @Nullable
  @Length(max = 50)
  private String name;

  @Nullable
  @Length(max = 50)
  private String address;

  public RegistrationInfo(String username, String password, String name, String address) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.address = address;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
