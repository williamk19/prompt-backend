package com.restful.promptbackend.Task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

//    Optional<Task> findById(String task);
}