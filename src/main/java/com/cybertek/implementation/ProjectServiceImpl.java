package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.Project;
import com.cybertek.entity.User;
import com.cybertek.enums.Status;
import com.cybertek.mapper.ProjectMapper;
import com.cybertek.mapper.UserMapper;
import com.cybertek.repository.ProjectRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectMapper projectMapper, ProjectRepository projectRepository, UserMapper userMapper, UserService userService, TaskService taskService) {
        this.projectMapper = projectMapper;
        this.projectRepository = projectRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return projectMapper.convertToDto(projectRepository.findByProjectCode(code));
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll(Sort.by("projectCode")).stream().map(obj -> {return projectMapper.convertToDto(obj);}).collect(Collectors.toList());
    }

    @Override
    public Project save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project obj = projectMapper.convertToEntity(dto);
        obj.setAssignedManager(userMapper.convertToEntity(dto.getAssignedManager()));
        return projectRepository.save(obj);
    }

    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedProject = projectMapper.convertToEntity(dto);
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        projectRepository.save(convertedProject);
    }

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode() + "-" + project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDto(project));

    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        UserDTO currentUserDTO = userService.findByUserName("your.email+fakedata56150@gmail.com");
        User user = userMapper.convertToEntity(currentUserDTO);
        List<Project> projects = projectRepository.findAllByAssignedManager(user);

        return projects.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDto(project);
            obj.setUnfinishedTaskCount(taskService.totalNonCompletedTasks(project.getProjectCode()));
            obj.setCompleteTaskCount(taskService.totalCompletedTasks(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());

    }

}
