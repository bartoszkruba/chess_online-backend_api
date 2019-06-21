package com.company.chess_online_bakend_api.data.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    String description;

    @Override
    @Transactional
    public String getAuthority() {
        return description;
    }

    @Builder
    public Role(Long id, LocalDateTime created, LocalDateTime updated, String description) {
        super(id, created, updated);
        this.description = description;
    }
}
