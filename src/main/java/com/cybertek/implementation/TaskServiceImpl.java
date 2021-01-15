package com.cybertek.implementation;

import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Task;
import com.cybertek.enums.Status;
import com.cybertek.mapper.TaskMapper;
import com.cybertek.repository.TaskRepository;
import com.cybertek.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO findById(Long id) {
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> list = taskRepository.findAll();
        return list.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());    // Double Colon Operator.
//        return list.stream().map(obj -> {return taskMapper.convertToDTO(obj);}).collect(Collectors.toList());
    }

    @Override
    public Task save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        return taskRepository.save(task);
    }

    @Override
    public void update(TaskDTO dto) {

    }

    @Override
    public void delete(Long id) {
        Optional<Task> foundedTask = taskRepository.findById(id);
        if (foundedTask.isPresent()) {
            foundedTask.get().setIsDeleted(true);
            taskRepository.save(foundedTask.get());
        }
    }

}
