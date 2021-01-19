package com.cybertek.service;

import com.cybertek.dto.UserDTO;
import com.cybertek.entity.User;
import com.cybertek.exception.TicketingProjectException;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO dto);
    UserDTO update(UserDTO dto);
    void delete(String username) throws TicketingProjectException;
    void deleteByUserName(String username);
    List<UserDTO> listAllByRole(String role);
    Boolean checkIfUserCanBeDeleted(User user);

}
