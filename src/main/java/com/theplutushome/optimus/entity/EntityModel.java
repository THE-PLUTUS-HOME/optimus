package com.theplutushome.optimus.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class EntityModel {
    @Id
    private String id;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean deleted;
    private String deletedBy;
}
