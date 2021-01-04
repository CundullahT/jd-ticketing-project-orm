package com.cybertek.service;

import com.cybertek.dto.ProjectDTO;

public interface ProjectService extends CrudService<ProjectDTO,String> {
    void complete(ProjectDTO project);
}
