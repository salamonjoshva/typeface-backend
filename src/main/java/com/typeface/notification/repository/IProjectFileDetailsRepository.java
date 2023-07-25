package com.typeface.notification.repository;

import com.typeface.notification.entity.ProjectFileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProjectFileDetailsRepository extends JpaRepository<ProjectFileDetails,Long> {

    @Query("from ProjectFileDetails pd join pd.projectDetail where pd.projectDetail.id = :projectId")
    List<ProjectFileDetails> findByProjectDetail(Long projectId);
}
