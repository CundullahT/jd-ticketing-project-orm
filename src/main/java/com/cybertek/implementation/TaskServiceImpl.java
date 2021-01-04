package com.cybertek.implementation;

import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.enums.Status;
import com.cybertek.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends AbstractMapService<TaskDTO,Long> implements TaskService {
    @Override
    public List<TaskDTO> findAll() {
        return super.findAll();
    }

    @Override
    public TaskDTO save(TaskDTO object) {
        return super.save(object.getId(), object);
    }

    @Override
    public void update(TaskDTO task) {

        TaskDTO foundTask = findById(task.getId());
        foundTask.setTaskStatus(task.getTaskStatus());

        super.update(foundTask.getId(), foundTask);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public void delete(TaskDTO object) {
        super.delete(object);
    }

    @Override
    public TaskDTO findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<TaskDTO> findTaskByManager(UserDTO manager) {
        return super.findAll().stream().filter(task -> task.getProject().getAssignedManager().equals(manager)).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findTaskByEmployee(UserDTO employee) {
        return super.findAll().stream().filter(task -> task.getAssignedEmployee().equals(employee)).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findTaskByStatus(Status status) {
        return super.findAll().stream().filter(task -> task.getTaskStatus().equals(status)).collect(Collectors.toList());
    }

}
