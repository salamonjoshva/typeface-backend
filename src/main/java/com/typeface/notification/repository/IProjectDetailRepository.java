package com.typeface.notification.repository;

import com.typeface.notification.entity.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProjectDetailRepository extends JpaRepository<ProjectDetail,Long> {

    @Query(value = "select * from project_details pd where pd.user_id=:userId and pd.id > :id limit 20",nativeQuery = true)
    List<ProjectDetail> getProjectDetail(Long userId,Long id);

    @Query(value = "select * from project_details pd where pd.user_id=:userId and pd.id > :id order by pd.created_at desc limit 20",nativeQuery = true)
    List<ProjectDetail> getProjectDetailByDsc(Long userId,Long id);


    @Query(value = "select count(pd.id) from project_details pd where pd.user_id=:userId",nativeQuery = true)
    Long getProjectDetailCount(Long userId);

}
