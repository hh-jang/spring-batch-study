package com.hhjang.springbatch.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PayRepositoryTest {
    @Autowired
    private PayRepository repository;

    @Test
    public void create() {
        // Given
        Pay pay = new Pay();
        pay.setAmount(1000L);
        pay.setTxName("test tx");
        pay.setTxDateTime(LocalDateTime.now());
        pay.setSuccessStatus(true);

        // When
        Pay savedPay = repository.save(pay);

        // Then
        assertThat(savedPay.getAmount()).isEqualTo(1000L);
        assertThat(savedPay.getTxName()).isEqualTo("test tx");
    }

    @Test
    public void findAllBySuccessStatus() {
        // Given, When
        List<Pay> allBySuccessStatus = repository.findAllSuccess();

        // Then
        assertThat(allBySuccessStatus.size()).isEqualTo(1);
    }
}