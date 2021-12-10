package com.eksamen.projectcalculator.controller;

import com.eksamen.projectcalculator.domain.service.ProjectService;
import com.eksamen.projectcalculator.domain.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.text.ParseException;

@Controller
public class TaskController {

    private final TaskService TASK_SERVICE = new TaskService();
    private final ProjectService PROJECT_SERVICE = new ProjectService();

    @GetMapping("/project/add")
    public String addTask(@RequestParam(name="id") long id, Model model, WebRequest request) {
        Long userId = (Long) request.getAttribute("userId", WebRequest.SCOPE_SESSION);
        if (userId == null) return "login";

        if (PROJECT_SERVICE.projectIsUsers(userId, id)) {
            model.addAttribute("id", id);
            return "addTask";
        } else {
            return "error";
        }
    }

    @PostMapping("/project/addTaskVerify")
    public String addTaskVerify(@RequestParam(name="id") long id, WebRequest request, RedirectAttributes redirectAttributes) throws ParseException {
        Long userId = (Long) request.getAttribute("userId", WebRequest.SCOPE_SESSION);
        if (userId == null) return "login";

        if (PROJECT_SERVICE.projectIsUsers(userId, id)) {
            String taskName = request.getParameter("name");
            String resource = request.getParameter("resource");
            String startDate = request.getParameter("start");
            String finishDate = request.getParameter("finish");
            int completion = Integer.parseInt(request.getParameter("completion"));
            double dailyWorkHours = Double.parseDouble(request.getParameter("hours"));
            double pricePerHour = Double.parseDouble(request.getParameter("priceprhr"));

            TASK_SERVICE.createTask(id, taskName, resource, startDate, finishDate, completion, dailyWorkHours, pricePerHour);
            redirectAttributes.addAttribute("id", id);

            return "redirect:/project";
        } else {
            return "error";
        }
    }

    @GetMapping("/project/task")
    public String inspectTask(@RequestParam long id, Model model, WebRequest request) {
        Long userId = (Long) request.getAttribute("userId", WebRequest.SCOPE_SESSION);
        if (userId == null) return "login";

        if (TASK_SERVICE.taskIsUsers(userId, id)) {
            model.addAttribute("task", TASK_SERVICE.getTaskById(id));
            return "inspectTask";
        } else {
            System.out.println("NOT USERS");
            return "error";
        }
    }

    @GetMapping("/project/clear/confirm")
    public String clearConfirm(@RequestParam(name = "id") long id, RedirectAttributes redirectAttributes, WebRequest request) {
        Long userId = (Long) request.getAttribute("userId", WebRequest.SCOPE_SESSION);
        if (userId == null) return "login";

        if (PROJECT_SERVICE.projectIsUsers(userId, id)) {
            TASK_SERVICE.clearTasksByProjectId(id);
            redirectAttributes.addAttribute("id", id);
            return "redirect:/project";
        } else {
            return "error";
        }
    }

    @GetMapping("/project/task/delete")
    public String deleteTask(@RequestParam(name = "id") long id, WebRequest request, Model model) {
        Long userId = (Long) request.getAttribute("userId", WebRequest.SCOPE_SESSION);
        if (userId == null) return "login";

        if (TASK_SERVICE.taskIsUsers(userId, id)) {
            model.addAttribute("taskId", id);
            model.addAttribute("message", "Are you sure? This will delete the task.");
            return "deleteTask";
        } else {
            return "error";
        }
    }

    @GetMapping("/project/task/delete-confirm")
    public String deleteTaskConfirm(@RequestParam(name = "id") long id, WebRequest request, RedirectAttributes redirectAttributes)  {
        Long userId = (Long) request.getAttribute("userId", WebRequest.SCOPE_SESSION);
        if (userId == null) return "login";

        if (TASK_SERVICE.taskIsUsers(userId, id)) {
            long projectId = TASK_SERVICE.getProjectIdById(id);
            redirectAttributes.addAttribute("id", projectId);
            TASK_SERVICE.deleteTaskById(id);
            return "redirect:/project";
        } else {
            return "error";
        }
    }
}
