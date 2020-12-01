package com.hhjang.springbatch.entity.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PaySumRepository extends JpaRepository<PaySum, Long> {

    @Query("SELECT p FROM PaySum p WHERE p.tradeDate =:tradeDate")
    PaySum findAllByTradeDate(LocalDate tradeDate);
}
