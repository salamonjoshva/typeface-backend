package com.typeface.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "project_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @Column(name = "name",unique = true)
    private String projectName;
    @Column(name = "description")
    private String description;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;


}
