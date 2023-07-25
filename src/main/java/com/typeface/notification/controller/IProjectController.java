package com.typeface.notification.controller;

import com.typeface.notification.entity.*;
import com.typeface.notification.model.ProjectDetailsResponse;
import com.typeface.notification.model.ProjectFileDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/v1")
@CrossOrigin
public interface IProjectController {

    @PostMapping("/create/user")
    User createUser(@RequestBody  User user);

    @PostMapping("/create/project")
    ProjectDetail createProjectDetail(@RequestBody ProjectDetail projectDetail);

    @PostMapping("/create/projectFile")
    ProjectFileDetails createProjectFileDetail(@RequestBody ProjectFileDetails projectFileDetail);

    @GetMapping("/project")
    ProjectDetailsResponse getProjectDetails(@RequestParam(required = false) String nextOffset,@RequestParam(required = false) String filter) throws Exception;

    @GetMapping("/list/projectFiles")
    List<ProjectFileDetailResponse> getProjectFiles(@RequestHeader Long projectId);

    @PostMapping("/upload/file")
    ResponseEntity<Void> uploadFile(@RequestHeader Long projectId);
}
