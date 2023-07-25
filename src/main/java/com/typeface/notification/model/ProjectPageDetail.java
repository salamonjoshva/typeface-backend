package com.typeface.notification.model;

public class ProjectPageDetail {
    private Long nextOffsetId;
    private Long currentPageNumber;

    public Long getNextOffsetId() {
        return nextOffsetId;
    }

    public void setNextOffsetId(Long nextOffsetId) {
        this.nextOffsetId = nextOffsetId;
    }

    public Long getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(Long currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }
}
