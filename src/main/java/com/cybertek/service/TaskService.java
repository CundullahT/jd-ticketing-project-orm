package com.cybertek.service;

import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Task;

import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id);
    List<TaskDTO> listAllTasks();
    Task save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);

}
