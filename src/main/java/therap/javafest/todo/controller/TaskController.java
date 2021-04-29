package therap.javafest.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author erfan
 * @since 4/16/21
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController {

    private static final String SHOW_PAGE = "task/show";

    @GetMapping(value = "/show")
    public String show() {

        return SHOW_PAGE;
    }
}
