package therap.javafest.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import therap.javafest.todo.domain.Task;
import therap.javafest.todo.domain.User;
import therap.javafest.todo.service.TaskService;
import therap.javafest.todo.service.UserService;

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
    public Task save(@RequestBody Task task) {
        User user = getLoggedInUser();
        task.setUser(user);
        taskService.save(task);

        return task;
    }

    private User getLoggedInUser() {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}