package com.restful.promptbackend.Task;

import com.restful.promptbackend.User.User;
import com.restful.promptbackend.User.UserRepository;
import com.restful.promptbackend.jwt.JwtTokenUtil;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/task")
public class TaskController {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @GetMapping("/list")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM"})
  public List<Task> listAll() {
    return taskRepository.findAll();
  }

  @GetMapping("/user/{username}")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> listByUser(
    @PathVariable("username") String username,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    String uname = jwtTokenUtil.getUsername(token.split(" ")[1].trim());
    String role = jwtTokenUtil.getRoles(token.split(" ")[1].trim())[0];

    if (userRepository.findByUsername(username).isPresent()) {
      User user = userRepository.findByUsername(username).get();
      if (!Objects.equals(role, "ROLE_EMPLOYEE")) {
        return new ResponseEntity<>(user, HttpStatus.OK);
      } else {
        if (Objects.equals(user.getUsername(), uname)) {
          return new ResponseEntity<>(user.getTasks(), HttpStatus.OK);
        }
      }
    }
    return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
  }

  @GetMapping("/{id}")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM", "ROLE_EMPLOYEE"})
  public ResponseEntity<?> getTask(
    @PathVariable("id") Integer task_id,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String token
  ) {
    String username = jwtTokenUtil.getUsername(token.split(" ")[1].trim());
    String role = jwtTokenUtil.getRoles(token.split(" ")[1].trim())[0];

    if (taskRepository.findById(task_id).isPresent()) {
      Task task = taskRepository.findById(task_id).get();
      if (!Objects.equals(role, "ROLE_EMPLOYEE")) {
        return new ResponseEntity<>(task, HttpStatus.OK);
      } else {
        if (Objects.equals(task.getUser().getUsername(), username)) {
          return new ResponseEntity<>(task, HttpStatus.OK);
        }
      }
    }

    return ResponseEntity.notFound().build();
  }

  @PostMapping("/create")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM"})
  public ResponseEntity<?> createNewTask(@RequestBody @Valid TaskRequest taskRequest) {
    Optional<User> user = userRepository.findById(taskRequest.getUserId());
    if (user.isPresent()) {
      User userAssigned = user.get();
      Task task = new Task(
        taskRequest.getTitleTask(),
        userAssigned,
        taskRequest.getDescTask(),
        taskRequest.getStatus()
      );
      Task savedTask = taskRepository.save(task);
      URI newTaskURI = URI.create("/task/" + savedTask.getId());
      return new ResponseEntity<>(user, HttpStatus.OK);
    }


    return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
  }

  @PostMapping("/{id}/update")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM", "ROLE_EMPLOYEE"})
  public ResponseEntity<Task> updateTask(
    @PathVariable("id") Integer task_id,
    @RequestBody @Valid TaskRequest taskRequest
  ) {
    Optional<Task> taskData = taskRepository.findById(task_id);
    Optional<User> user = userRepository.findById(taskRequest.getUserId());
    if (taskData.isPresent() && user.isPresent()) {
      Task updateTask = taskData.get();
      User updateUserAssigned = user.get();
      updateTask.setTitleTask(taskRequest.getTitleTask());
      updateTask.setDescTask(taskRequest.getDescTask());
      updateTask.setStatus(taskRequest.getStatus());
      updateTask.setUser(updateUserAssigned);
      Task updatedTask = taskRepository.save(updateTask);
      return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/{id}/delete")
  @RolesAllowed({"ROLE_ADMIN", "ROLE_PM"})
  public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") Integer task_id) {
    Optional<Task> taskData = taskRepository.findById(task_id);
    if (taskData.isPresent()) {
      taskRepository.deleteById(task_id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}
