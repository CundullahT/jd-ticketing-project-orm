package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Task;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.mapper.ProjectMapper;
import com.cybertek.mapper.TaskMapper;
import com.cybertek.repository.TaskRepository;
import com.cybertek.repository.UserRepository;
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
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> foundedTask = taskRepository.findById(id);
        return foundedTask.map(taskMapper::convertToDTO).orElse(null);
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> list = taskRepository.findAll();
        return list.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());    // Double Colon Operator.
//        return list.stream().map(obj -> {return taskMapper.convertToDTO(obj);}).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllByProject(ProjectDTO projectDTO) {
        List<Task> tasks = taskRepository.findAllByProject(projectMapper.convertToEntity(projectDTO));
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {

        User user = userRepository.findByUserName("your.email+fakedata14407@gmail.com");
        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, user);

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public List<TaskDTO> listAllTasksByProjectManager() {

        User user = userRepository.findByUserName("your.email+fakedata88540@gmail.com");
        List<Task> tasks = taskRepository.findAllByProjectAssignedManager(user);

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateStatus(TaskDTO dto) {
        Optional<Task> foundedTask = taskRepository.findById(dto.getId());

        if(foundedTask.isPresent()){
            foundedTask.get().setTaskStatus(dto.getTaskStatus());
            taskRepository.save(foundedTask.get());
        }

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
        Optional<Task> foundedTask = taskRepository.findById(dto.getId());
        Task convertedTask = taskMapper.convertToEntity(dto);

        if(foundedTask.isPresent()){
            convertedTask.setId(foundedTask.get().getId());
            convertedTask.setTaskStatus(foundedTask.get().getTaskStatus());
            convertedTask.setAssignedDate(foundedTask.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Task> foundedTask = taskRepository.findById(id);
        if (foundedTask.isPresent()) {
            foundedTask.get().setIsDeleted(true);
            taskRepository.save(foundedTask.get());
        }
    }

    @Override
    public int totalNonCompletedTasks(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTasks(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {

        List<TaskDTO> taskDTOS = listAllByProject(projectDTO);
        taskDTOS.forEach(taskDTO -> delete(taskDTO.getId()));

    }

}
