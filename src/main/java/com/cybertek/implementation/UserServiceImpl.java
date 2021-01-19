package com.cybertek.implementation;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.dto.TaskDTO;
import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;
import com.cybertek.mapper.MapperUtil;
import com.cybertek.repository.UserRepository;
import com.cybertek.service.ProjectService;
import com.cybertek.service.TaskService;
import com.cybertek.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, @Lazy ProjectService projectService, TaskService taskService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(obj -> {return mapperUtil.convert(obj, new UserDTO());}).collect(Collectors.toList()); // We can not use mapperUtil::convert since we need to use parameters.
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public void save(UserDTO dto) {
        userRepository.save(mapperUtil.convert(dto, new User()));
    }

    @Override
    public UserDTO update(UserDTO dto) {

        // Find current user
        User user = userRepository.findByUserName(dto.getUserName());

        // Map update user dto to entity object
        User convertedUser = mapperUtil.convert(dto, new User());

        // Set id to the converted object
        convertedUser.setId(user.getId());

        // Save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    // Soft Delete

    @Override
    public void delete(String username) throws TicketingProjectException {
        User user = userRepository.findByUserName(username);

        if (user == null) throw new TicketingProjectException("User Does Not Exists.");

        if (!checkIfUserCanBeDeleted(user)) throw new TicketingProjectException("User Can Not Be Deleted. It Is Linked By A Project Or Task.");

        user.setUserName(user.getUserName() + "-" + user.getId());
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    // Hard Delete
    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);
        return users.stream().map(obj -> {return mapperUtil.convert(obj, new UserDTO());}).collect(Collectors.toList());
    }

    @Override
    public Boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()){

            case "Manager":
                List<ProjectDTO> projectList = projectService.readAllByAssignedManager(user);
                return projectList.size() == 0;
            case "Employee":
                List<TaskDTO> taskList = taskService.readAllByEmployee(user);
                return taskList.size() == 0;
            default:
                return true;
        }

    }

}
