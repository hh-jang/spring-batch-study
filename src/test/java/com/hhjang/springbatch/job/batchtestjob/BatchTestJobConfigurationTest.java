package com.hhjang.springbatch.job.batchtestjob;

import com.hhjang.springbatch.config.TestBatchConfig;
import com.hhjang.springbatch.entity.pay.Pay;
import com.hhjang.springbatch.entity.pay.PayRepository;
import com.hhjang.springbatch.entity.pay.PaySum;
import com.hhjang.springbatch.entity.pay.PaySumRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.hhjang.springbatch.job.batchtestjob.BatchTestJobConfiguration.FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BatchTestJobConfiguration.class, TestBatchConfig.class})
@SpringBatchTest
@ActiveProfiles("local")
public class BatchTestJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PaySumRepository paySumRepository;

    @Test
    @DisplayName("특정 날짜에 발생한 거래의 금액 합을 저장한다")
    public void getSumOfAmountByTradeDate() throws Exception {
        // Given
        LocalDate tradeDate = LocalDate.of(2020, 12, 1);

        Pay testPay1 = Pay.builder()
                .txName("테스트 거래1")
                .amount(1000L)
                .txLocalDate(tradeDate)
                .build();
        Pay testPay2 = Pay.builder()
                .txName("테스트 거래2")
                .amount(2000L)
                .txLocalDate(tradeDate)
                .build();
        Pay testPay3 = Pay.builder()
                .txName("테스트 거래3")
                .amount(3000L)
                .txLocalDate(tradeDate)
                .build();

        payRepository.save(testPay1);
        payRepository.save(testPay2);
        payRepository.save(testPay3);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("tradeDate", tradeDate.format(FORMATTER))
                .toJobParameters();

        // 배치 실행
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters); // (3)
        PaySum allByTradeDate = paySumRepository.findAllByTradeDate(tradeDate);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(allByTradeDate.getAmountSum()).isEqualTo(6000L);
    }
}