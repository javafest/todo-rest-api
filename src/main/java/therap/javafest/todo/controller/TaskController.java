package therap.javafest.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author erfan
 * @since 4/16/21
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController {

    private static final String SHOW_PAGE = "task/show";
    private static final String EDIT_PAGE = "task/edit";

    @GetMapping(value = "/show")
    public String show() {

        return SHOW_PAGE;
    }

    @GetMapping(value = "/edit")
    public String edit(@RequestParam("id") int id, ModelMap model) {
        model.put("id", id);

        return EDIT_PAGE;
    }
}
