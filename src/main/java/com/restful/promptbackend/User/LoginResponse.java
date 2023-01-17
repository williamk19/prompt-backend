package com.restful.promptbackend.User;

public class LoginResponse {
  private String username;
  private String accessToken;

  private String role;

  public LoginResponse(String username, String role, String accessToken) {
    this.username = username;
    this.role = role;
    this.accessToken = accessToken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
