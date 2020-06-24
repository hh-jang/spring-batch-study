package com.hhjang.springbatch.job.itemreader;

import com.hhjang.springbatch.entity.Pay;
import com.hhjang.springbatch.entity.PayRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name=" + JpaPagingItemReaderJobConfiguration.JOB_NAME})
public class JpaPagingItemReaderJobConfigurationTest {
    @Autowired
    private PayRepository repository;

    @Autowired
    public JobLauncherTestUtils jobLauncherTestUtils;

    @Qualifier(value = JpaPagingItemReaderJobConfiguration.JOB_NAME)
    private Job job;

    @BeforeEach
    private void initJobLauncherTestUtiles() {
        this.jobLauncherTestUtils.setJob(job);
    }

    @Test
    public void test() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        List<Pay> successPay = repository.findAllBySuccessStatus();

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(successPay.size()).isEqualTo(4);
    }
}