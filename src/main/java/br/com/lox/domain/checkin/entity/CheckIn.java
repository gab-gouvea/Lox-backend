package br.com.lox.domain.checkin.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity

public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String instructions;
    private LocalDateTime checkinDate;
}
