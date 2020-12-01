package com.hhjang.springbatch.entity.pay;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Pay {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    private boolean successStatus;

    @Column(length=255)
    private String txName;

    private LocalDate txDate;

    public Pay(Long amount, boolean successStatus, String txName, LocalDate txLocalDate) {
        this.amount = amount;
        this.successStatus = successStatus;
        this.txName = txName;
        this.txDate = txLocalDate;
    }

    public Pay(Long amount, boolean successStatus, String txName, String txLocalDate) {
        this.amount = amount;
        this.successStatus = successStatus;
        this.txName = txName;
        this.txDate = LocalDate.parse(txLocalDate, FORMATTER);
    }

    public Pay(Long id, Long amount, boolean successStatus, String txName, String txLocalDate) {
        this.id = id;
        this.amount = amount;
        this.successStatus = successStatus;
        this.txName = txName;
        this.txDate = LocalDate.parse(txLocalDate, FORMATTER);
    }

    public Pay(Long id, boolean successStatus) {
        this.id = id;
        this.successStatus = successStatus;
    }

    @Builder
    public Pay(Long amount, String txName, LocalDate txLocalDate) {
        this.amount = amount;
        this.txName = txName;
        this.txDate = txLocalDate;
    }

    public void success() {this.successStatus = true;}
}
