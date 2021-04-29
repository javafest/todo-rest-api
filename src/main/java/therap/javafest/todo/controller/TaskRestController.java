package therap.javafest.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import therap.javafest.todo.domain.Task;
import therap.javafest.todo.domain.User;
import therap.javafest.todo.service.TaskService;
import therap.javafest.todo.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author mahfuz.ahmed
 * @since 4/27/21
 */
@RestController
@RequestMapping("/tasks")
public class TaskRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> show() {
        User user = getLoggedInUser();

        return taskService.getAllTasks(user);
    }

    @PostMapping
    public Task save(@Valid @RequestBody Task task) {
        User user = getLoggedInUser();
        task.setUser(user);
        taskService.save(task);

        return task;
    }

    @GetMapping(value = "/{id}")
    public Task show(@PathVariable int id) {

        return taskService.getById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable int id, @Valid @RequestBody Task task) {
        Task existingTask = taskService.getById(id);
        existingTask.setName(task.getName());
        taskService.save(existingTask);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        taskService.delete(taskService.getById(id));

        return ResponseEntity.ok().build();
    }

    private User getLoggedInUser() {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}