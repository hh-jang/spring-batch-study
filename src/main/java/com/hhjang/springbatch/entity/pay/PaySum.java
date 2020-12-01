package com.hhjang.springbatch.entity.pay;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PaySum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amountSum;

    private LocalDate tradeDate;

    @Builder
    public PaySum(LocalDate tradeDate, Long amountSum) {
        this.amountSum = amountSum;
        this.tradeDate = tradeDate;
    }
}
