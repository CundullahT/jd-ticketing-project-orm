package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;
import com.cybertek.entity.Project;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    Project save(ProjectDTO dto);
    void update(ProjectDTO dto);
    void delete(String code);
    void complete(String projectCode);
    List<ProjectDTO> listAllProjectDetails();

}
