package com.typeface.notification.model;

import com.typeface.notification.entity.ProjectDetail;

import java.util.List;

public class ProjectDetailsResponse {
    private List<ProjectDetail> projectDetailList;
    private String nextOffset;
    private Long currentPageNumber;
    private Long totalCount;
    private boolean hasMore;

    public List<ProjectDetail> getProjectDetailList() {
        return projectDetailList;
    }

    public void setProjectDetailList(List<ProjectDetail> projectDetailList) {
        this.projectDetailList = projectDetailList;
    }

    public String getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(String nextOffset) {
        this.nextOffset = nextOffset;
    }

    public Long getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(Long currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
