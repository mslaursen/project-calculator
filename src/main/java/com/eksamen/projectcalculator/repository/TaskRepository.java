package com.eksamen.projectcalculator.repository;

import com.eksamen.projectcalculator.domain.model.Project;
import com.eksamen.projectcalculator.domain.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {
    public void createTask(long projectId, String taskName, String resource, String startDate, String finishDate, int completion) {
        try {
            Connection connection = DBManager.getConnection();
            String query = "INSERT INTO task(project_id, task_name, resource, start_date, finish_date, percent_complete) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, projectId);
            preparedStatement.setString(2, taskName);
            preparedStatement.setString(3, resource);
            preparedStatement.setString(4, startDate);
            preparedStatement.setString(5, finishDate);
            preparedStatement.setInt(6, completion);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTasksByProjectId(long id) {
        try {
            Connection connection = DBManager.getConnection();
            List<Task> tasks = new ArrayList<>();
            String query = "SELECT * FROM task WHERE project_id = " + id;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Task task = new Task();
                task.setTaskId(resultSet.getLong("task_id"));
                task.setProjectId(resultSet.getLong("project_id"));
                task.setTaskName(resultSet.getString("task_name"));
                task.setResource(resultSet.getString("resource"));

                String startDate = resultSet.getString("start_date");
                String finishDate = resultSet.getString("finish_date");

                task.setStartDateStr(String.join(" ", startDate.split("-")));
                task.setFinishDateStr(String.join(" ", finishDate.split("-")));

                task.setPercentComplete(resultSet.getInt("percent_complete"));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}