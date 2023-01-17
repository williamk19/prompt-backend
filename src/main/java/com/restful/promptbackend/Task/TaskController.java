package com.restful.promptbackend.Task;

import com.restful.promptbackend.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/list")
    @CrossOrigin
    @RolesAllowed({"SUPER_ADMIN", "PROJECT_MANAGER", "KARYAWAN"})
    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "PROJECT_MANAGER", "KARYAWAN"})
    public ResponseEntity<Optional<Task>> getTask(@PathVariable("id") Integer task_id) {
        if (taskRepository.findById(task_id).isPresent()) {
            return new ResponseEntity<>(taskRepository.findById(task_id), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    @RolesAllowed({"SUPER_ADMIN", "PROJECT_MANAGER"})
    public ResponseEntity<Task> createNewTask(@RequestBody @Valid Task newTaskData) {
        Task saveTask = taskRepository.save(newTaskData);
        URI newTaskURI = URI.create("{id}" + saveTask.getId());
        return ResponseEntity.created(newTaskURI).body(saveTask);
    }

    @PostMapping("/update")
    @RolesAllowed({"SUPER_ADMIN", "PROJECT_MANAGER"})
    public ResponseEntity<Task> updateTask(@RequestBody @PathVariable("id") Integer task_id) {
        Optional<Task> taskData = taskRepository.findById(task_id);
        if (taskData.isPresent()) {
            Task _task = taskData.get();
            _task.setTitleTask(_task.getTitleTask());
            _task.setDescTask(_task.getDescTask());
            _task.setStatus(_task.getStatus());
            _task.setUsers((_task.getStatus()));

            return new ResponseEntity<>(taskRepository.save(_task), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/delete")
    @RolesAllowed({"SUPER_ADMIN", "PROJECT_MANAGER"})
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
