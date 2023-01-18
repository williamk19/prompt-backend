package com.restful.promptbackend.User;

import javax.persistence.*;

import com.restful.promptbackend.Role.Role;
import com.restful.promptbackend.Task.Task;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

  @ManyToMany
  @JoinTable(
    name = "users_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  @OneToMany (targetEntity = Task.class)
  private Set<Task> tasks;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true, length = 16)
  private String username;

  @Column(nullable = false, length = 64)
  private String password;

  @Column(nullable = true, length = 50)
  private String name;

  @Column(nullable = true, length = 50)
  private String email;

  public User() {
  }

  public User(String username, String password, String name, String email) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.email = email;
    this.roles = new HashSet<Role>();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
    }
    return authorities;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void resetRole() {
    this.roles.clear();
  }

  public Set<Task> getTasks() {
    return tasks;
  }

  public void setTasks(Set<Task> tasks) {
    this.tasks = tasks;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
