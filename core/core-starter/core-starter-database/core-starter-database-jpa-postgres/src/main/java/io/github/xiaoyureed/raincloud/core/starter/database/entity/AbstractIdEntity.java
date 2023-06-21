package io.github.xiaoyureed.raincloud.core.starter.database.entity;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractIdEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;
}
