package therap.javafest.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import therap.javafest.todo.domain.Task;
import therap.javafest.todo.domain.User;
import therap.javafest.todo.service.TaskService;
import therap.javafest.todo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author mahfuz.ahmed
 * @since 4/27/21
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @GetMapping
    public CollectionModel<EntityModel<Task>> getAll() {
        User user = getLoggedInUser();

        List<EntityModel<Task>> taskList = taskService.getAllTasks(user).stream()
                .map(this::getEntityModel)
                .collect(Collectors.toList());

        return CollectionModel.of(taskList,
                linkTo(methodOn(TaskRestController.class).getAll()).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    public EntityModel<Task> getOne(@PathVariable int id) {
        Task task = taskService.getById(id);

        return getEntityModel(task);
    }

    @PostMapping
    public EntityModel<Task> save(@Valid @RequestBody Task task) {
        User user = getLoggedInUser();
        task.setUser(user);
        taskService.save(task);

        return getEntityModel(task);
    }

    @PutMapping(value = "/{id}")
    public EntityModel<Task> update(@PathVariable int id, @Valid @RequestBody Task task) {
        Task existingTask = taskService.getById(id);
        existingTask.setName(task.getName());
        taskService.save(existingTask);

        return getEntityModel(existingTask);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Task> delete(@PathVariable int id) {
        taskService.delete(taskService.getById(id));

        return ResponseEntity.ok().build();
    }

    private User getLoggedInUser() {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private EntityModel<Task> getEntityModel(Task task) {
        return EntityModel.of(task,
                linkTo(methodOn(TaskRestController.class).getOne(task.getId())).withSelfRel(),
                linkTo(methodOn(TaskRestController.class).getAll()).withRel("tasks"));
    }
}