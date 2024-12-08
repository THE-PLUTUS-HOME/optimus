package com.theplutushome.optimus.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class EntityModel {
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean deleted;
    private String deletedBy;
}
