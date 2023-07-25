package com.typeface.notification.model;

import com.typeface.notification.entity.ProjectDetail;

import java.util.Arrays;

public enum ProjectFilter {
    SORT_BY_CREATED_AT("sort_by_create_at");

    private String label;

    ProjectFilter(String label) {
        this.label = label;
    }

    public static ProjectFilter getEnumByLabel(String label) {
        return Arrays.stream(values()).filter(projectFilter -> projectFilter.getLabel().equalsIgnoreCase(label))
                .findFirst().orElse(null);
    }

    public String getLabel() {
        return label;
    }
}
