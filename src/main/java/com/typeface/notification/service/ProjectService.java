package com.typeface.notification.service;

import com.typeface.notification.entity.ProjectDetail;
import com.typeface.notification.entity.ProjectFileDetails;
import com.typeface.notification.entity.User;
import com.typeface.notification.model.ProjectDetailsResponse;
import com.typeface.notification.model.ProjectFileDetailResponse;
import com.typeface.notification.model.ProjectFilter;
import com.typeface.notification.repository.IProjectDetailRepository;
import com.typeface.notification.repository.IProjectFileDetailsRepository;
import com.typeface.notification.repository.IUserRepository;
import com.typeface.notification.util.ObjectSerializationUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    public static final String NEXT_OFFSET_ID = "nextOffsetId";
    public static final String CURRENT_PAGE_NUMBER = "currentPageNumber";
    private IUserRepository userRepository;
    private IProjectDetailRepository projectDetailRepository;
    private IProjectFileDetailsRepository projectFileDetailsRepository;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ProjectService(IUserRepository userRepository, IProjectDetailRepository projectDetailRepository, IProjectFileDetailsRepository projectFileDetailsRepository,
                          SimpMessagingTemplate messagingTemplate){
        this.projectDetailRepository = projectDetailRepository;
        this.projectFileDetailsRepository = projectFileDetailsRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public User createUserService(User user) {
        return this.userRepository.save(user);
    }

    public ProjectDetail createProjectDetailService(ProjectDetail projectDetail) {
        return this.projectDetailRepository.save(projectDetail);
    }

    public ProjectFileDetails createProjectFileDetailService(ProjectFileDetails projectFileDetail) {
        return this.projectFileDetailsRepository.save(projectFileDetail);
    }

    public ProjectDetailsResponse getProjectDetailService(Long userId,String nextOffSet,String filter) throws Exception {
        ProjectDetailsResponse response = new ProjectDetailsResponse();
        JSONObject pageDetails = new JSONObject();
        if(nextOffSet != null && !nextOffSet.isEmpty()) {
            pageDetails = ObjectSerializationUtil.deserializeStringToObject(nextOffSet);
        }
        List<ProjectDetail> projectDetails;
        if(filter != null && !filter.isEmpty() && ProjectFilter.getEnumByLabel(filter) != null) {
            // we can create switch case for each filter
            projectDetails = this.projectDetailRepository.getProjectDetailByDsc(userId, pageDetails.optLong(NEXT_OFFSET_ID, 0));
        }else {
            projectDetails = this.projectDetailRepository.getProjectDetail(userId, pageDetails.optLong(NEXT_OFFSET_ID, 0));
        }
        Long count = this.projectDetailRepository.getProjectDetailCount(userId);
        response.setProjectDetailList(projectDetails);
        response.setTotalCount(count);
        if(!projectDetails.isEmpty()) {
            JSONObject newPageDetail = new JSONObject();
            int totalPages = (int)Math.ceil((double)count/20);
            newPageDetail.put(NEXT_OFFSET_ID,projectDetails.get(projectDetails.size()-1).getId());
            newPageDetail.put(CURRENT_PAGE_NUMBER,pageDetails.optLong(CURRENT_PAGE_NUMBER,0)+1);
            response.setCurrentPageNumber(pageDetails.optLong(CURRENT_PAGE_NUMBER,0)+1);
            response.setHasMore(totalPages > (pageDetails.optLong(CURRENT_PAGE_NUMBER,0)+1));
            response.setNextOffset(ObjectSerializationUtil.serializeObjectToString(newPageDetail));
        }
        return response;
    }

    public List<ProjectFileDetailResponse> getProjectFileDetailService(Long projectId){
        List<ProjectFileDetailResponse> responses = new ArrayList<>();
       List<ProjectFileDetails> projectFileDetails = this.projectFileDetailsRepository.findByProjectDetail(projectId);
       projectFileDetails.stream().forEach(projectFileDetail -> {
           ProjectFileDetailResponse fileDetailResponse = new ProjectFileDetailResponse();
           fileDetailResponse.id = projectFileDetail.getId();
           fileDetailResponse.name = projectFileDetail.getFileName();
           fileDetailResponse.createdBy = projectFileDetail.getCreatedBy();
           responses.add(fileDetailResponse);
       } );
       return responses;
    }

    public void uploadFile() {
        // upload the file
        // insert the file detail in project detail table
//        messagingTemplate.convertAndSendToUser(JWTTokenUtility.getUserEmail(),"/topic/message", "File Uploaded");
        messagingTemplate.convertAndSend("/topic/message", "File Uploaded");
    }


}
