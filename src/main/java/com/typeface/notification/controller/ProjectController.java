package com.typeface.notification.controller;


import com.typeface.notification.configuration.JWTTokenUtility;
import com.typeface.notification.entity.ProjectDetail;
import com.typeface.notification.entity.ProjectFileDetails;
import com.typeface.notification.entity.User;
import com.typeface.notification.model.ProjectDetailsResponse;
import com.typeface.notification.model.ProjectFileDetailResponse;
import com.typeface.notification.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectController implements IProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @Override
    public User createUser(User user) {
        return this.projectService.createUserService(user);
    }

    @Override
    public ProjectDetail createProjectDetail(ProjectDetail projectDetail) {
        return this.projectService.createProjectDetailService(projectDetail);
    }

    @Override
    public ProjectFileDetails createProjectFileDetail(ProjectFileDetails projectFileDetail) {
        return this.projectService.createProjectFileDetailService(projectFileDetail);
    }

    @Override
    public ProjectDetailsResponse getProjectDetails(String nextOffset,String filter) throws Exception {
        return this.projectService.getProjectDetailService(JWTTokenUtility.getUserID(),nextOffset,filter);
    }

    @Override
    public List<ProjectFileDetailResponse> getProjectFiles(Long projectId) {
        return this.projectService.getProjectFileDetailService(projectId);
    }

    @Override
    public ResponseEntity<Void> uploadFile(Long projectId) {
        this.projectService.uploadFile();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
