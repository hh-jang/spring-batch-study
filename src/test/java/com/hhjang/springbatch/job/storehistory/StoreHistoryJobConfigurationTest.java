package com.hhjang.springbatch.job.storehistory;

import com.hhjang.springbatch.entity.employee.Employee;
import com.hhjang.springbatch.entity.product.Product;
import com.hhjang.springbatch.entity.store.Store;
import com.hhjang.springbatch.entity.store.StoreRepository;
import com.hhjang.springbatch.job.TestJobConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StoreHistoryJobConfiguration.class, TestJobConfiguration.class})
@ActiveProfiles("mysql")
@SpringBatchTest
public class StoreHistoryJobConfigurationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void copyToStoreHistoryFromStore() throws Exception {
        // Given
        Store store1 = new Store("서점", "서울시 강남구");
        store1.addProduct(new Product("책1_1", 10000L));
        store1.addProduct(new Product("책1_2", 20000L));
        store1.addEmployee(new Employee("직원1", LocalDate.now()));
        storeRepository.save(store1);
//
        Store store2 = new Store("서점2", "서울시 강남구");
        store2.addProduct(new Product("책2_1", 10000L));
        store2.addProduct(new Product("책2_2", 20000L));
        store2.addEmployee(new Employee("직원2", LocalDate.now()));
        storeRepository.save(store2);

        Store store3 = new Store("서점3", "서울시 강남구");
        store3.addProduct(new Product("책3_1", 10000L));
        store3.addProduct(new Product("책3_2", 20000L));
        store3.addEmployee(new Employee("직원3", LocalDate.now()));
        storeRepository.save(store3);

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        assertThat(jobExecution.getStatus()).isEqualTo(jobExecution.getStatus());
    }
}