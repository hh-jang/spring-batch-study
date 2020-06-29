package com.hhjang.springbatch.entity.pay;

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

    private boolean successStatus;

    @Column(length=255)
    private String txName;

    private LocalDateTime txDateTime;

    public Pay(Long amount, boolean successStatus, String txName, LocalDateTime txLocalDateTime) {
        this.amount = amount;
        this.successStatus = successStatus;
        this.txName = txName;
        this.txDateTime = txLocalDateTime;
    }

    public Pay(Long amount, boolean successStatus, String txName, String txLocalDateTime) {
        this.amount = amount;
        this.successStatus = successStatus;
        this.txName = txName;
        this.txDateTime = LocalDateTime.parse(txLocalDateTime, FORMATTER);
    }

    public Pay(Long id, Long amount, boolean successStatus, String txName, String txLocalDateTime) {
        this.id = id;
        this.amount = amount;
        this.successStatus = successStatus;
        this.txName = txName;
        this.txDateTime = LocalDateTime.parse(txLocalDateTime, FORMATTER);
    }

    public void success() {this.successStatus = true;}
}
