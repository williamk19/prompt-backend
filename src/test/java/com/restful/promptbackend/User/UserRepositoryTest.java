package com.restful.promptbackend.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
  @Autowired
  private UserRepository repo;

  @Test
  public void testCreateUser() {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String password = passwordEncoder.encode("williamk444");

    User newUser = new User("williamk444", password, "William", "Kurniawan");
    User savedUser = repo.save(newUser);

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getId()).isGreaterThan(0);
  }
}
