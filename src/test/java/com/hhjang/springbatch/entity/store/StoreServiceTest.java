package com.hhjang.springbatch.entity.store;

import com.hhjang.springbatch.job.TestJobConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StoreServiceTest {
    @Autowired
    private StoreService service;

    @Autowired
    private StoreRepository repository;

    @Test
    public void checkRepositoryBatchSize() {
        long productSumAndCheckEmployeesQuery = service.getProductSumAndCheckEmployeesQuery();
    }
}