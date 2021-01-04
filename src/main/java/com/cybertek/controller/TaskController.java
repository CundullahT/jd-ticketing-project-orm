package com.cybertek.controller;

import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.enums.Status;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @GetMapping("/create")
    public String createTask(Model model) {

        model.addAttribute("task", new TaskDTO());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployees());
        model.addAttribute("tasks", taskService.findAll());

        return "/task/create";

    }

    @PostMapping("/create")
    public String insertTask(TaskDTO task) {
        task.setTaskStatus(Status.OPEN);
        task.setAssignedDate(LocalDate.now());
        task.setId(UUID.randomUUID().getMostSignificantBits());

        taskService.save(task);
        return "redirect:/task/create";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteById(id);
        return "redirect:/task/create";
    }

    @GetMapping("/update/{id}")
    public String editTask(@PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployees());
        model.addAttribute("tasks", taskService.findAll());
        return "task/update";
    }

    @PostMapping("/update/{id}")
    public String updateTask(TaskDTO task) {
        taskService.update(task);
        return "redirect:/task/create";
    }

    @GetMapping("/pending")
    public String pendingTasks(Model model) {

        UserDTO employee = userService.findById("maria@cybertek.com");

        model.addAttribute("task", new TaskDTO());
        model.addAttribute("tasks", taskService.findTaskByEmployee(employee));
        model.addAttribute("statusList", Arrays.asList(Status.COMPLETE, Status.IN_PROGRESS, Status.OPEN, Status.UAT_TEST));
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployees());
        return "/employee/pending";
    }

    @GetMapping("/update/pending/{id}")
    public String editPendingTask(@ModelAttribute TaskDTO task, Model model) {

        UserDTO employee = userService.findById("maria@cybertek.com");

        model.addAttribute("task", task);
        model.addAttribute("tasks", taskService.findTaskByEmployee(employee));
        model.addAttribute("statusList", Arrays.asList(Status.COMPLETE, Status.IN_PROGRESS, Status.OPEN, Status.UAT_TEST));
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployees());
        return "/employee/update";
    }

    @PostMapping("/update/pending/{id}")
    public String updatePendingTask(TaskDTO task) {

        taskService.update(task);
        return "redirect:/task/pending";
    }

    @GetMapping("/archive")
    public String getCompletedTasks(Model model) {

        UserDTO employee = userService.findById("maria@cybertek.com");

        model.addAttribute("tasks", taskService.findTaskByStatus(Status.COMPLETE).stream().filter(task -> task.getAssignedEmployee().equals(employee)).collect(Collectors.toList()));
        return "/employee/archive";
    }

}
