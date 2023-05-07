package com.restful.promptbackend.User;

import com.restful.promptbackend.Role.Role;
import com.restful.promptbackend.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/test")
  public ResponseEntity<?> test() {
    Map<String, Object> map = new HashMap<>();
    map.put("server", "runnning");
    map.put("help", "https://github.com/williamk19/prompt-backend");

    Optional<User> user = userRepository.findByUsername("superadmin");
    if (user.isPresent()) {
      map.put("superadmin_init", "done");
    } else {
      String password = passwordEncoder.encode("superadmin");
      User newUser = new User(
        "superadmin",
        password,
        "superadmin",
        "superadmin@gmail.com"
      );
      newUser.addRole(new Role(1));
      map.put("superadmin_init", "created");
      User savedUser = userRepository.save(newUser);
    }

    return ResponseEntity.ok().body(map);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginInfo loginInfo) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getUsername(), loginInfo.getPassword()));

    User user = (User) authentication.getPrincipal();
    String accessToken = jwtTokenUtil.generateAccessToken(user);
    String role = user.getRoles().toArray()[0].toString();
    LoginResponse response = new LoginResponse(user.getUsername(), role, accessToken);

    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/auth/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegistrationInfo registrationInfo) {
    String password = passwordEncoder.encode(registrationInfo.getPassword());
    User newUser = new User(
      registrationInfo.getUsername(),
      password,
      registrationInfo.getName(),
      registrationInfo.getEmail()
    );
    newUser.addRole(new Role(3));

    User savedUser = userRepository.save(newUser);
    return new ResponseEntity<>("", HttpStatus.CREATED);
  }

  @GetMapping("/users/list")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM"})
  public List<User> userAll() {
    return userRepository.findAll();
  }

  @PostMapping("/users/{id}/role")
  @RolesAllowed({"ROLE_ADMIN"})
  public ResponseEntity<?> editUserRole(@PathVariable Integer id, @RequestBody Role role) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      User tempUser = user.get();
      int roleId = 0;
      if (Objects.equals(role.getRoleName(), "ROLE_ADMIN")) {
        roleId = 1;
      } else if (Objects.equals(role.getRoleName(), "ROLE_PM")) {
        roleId = 2;
      } else if (Objects.equals(role.getRoleName(), "ROLE_EMPLOYEE")) {
        roleId = 3;
      }
      tempUser.resetRole();
      tempUser.addRole(new Role(roleId));

      User updatedUser = userRepository.save(tempUser);
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
  }
}