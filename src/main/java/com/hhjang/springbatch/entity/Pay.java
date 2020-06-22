package com.hhjang.springbatch.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Pay {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    @Column(length=255)
    private String txName;

    private LocalDateTime txDateTime;

    public Pay(Long amount, String txName, LocalDateTime txLocalDateTime) {
        this.amount = amount;
        this.txName = txName;
        this.txDateTime = txDateTime;
    }

    public Pay(Long amount, String txName, String txLocalDateTime) {
        this.amount = amount;
        this.txName = txName;
        this.txDateTime = LocalDateTime.parse(txLocalDateTime, FORMATTER);
    }

    public Pay(Long id, Long amount, String txName, String txLocalDateTime) {
        this.id = id;
        this.amount = amount;
        this.txName = txName;
        this.txDateTime = LocalDateTime.parse(txLocalDateTime, FORMATTER);
    }
}
