package org.example.expert.domain.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long managerId;

    private String manager;

    private String action;

    private String message;

    private LocalDateTime creatAt = LocalDateTime.now();

    public Log() {}

    public Log(Long managerId, String manager, String action, String message) {
        this.managerId = managerId;
        this.manager = manager;
        this.action = action;
        this.message = message;
    }

}
