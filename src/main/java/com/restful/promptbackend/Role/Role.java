package com.restful.promptbackend.Role;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 100, unique = true)
  private String roleName;

  public Role() {}

  public Role(String roleName) {
    this.roleName = roleName;
  }
  public Role(Integer id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return this.roleName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
}
