package com.hhjang.springbatch.job.batchtestjob;

import com.hhjang.springbatch.entity.pay.Pay;
import com.hhjang.springbatch.entity.pay.PaySum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchTestJobConfiguration {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int CHUNK_SIZE = 50;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job batchTestJob() {
        return jobBuilderFactory.get("batchTestJob")
                .start(batchTestStep())
                .build();
    }

    private Step batchTestStep() {
        return stepBuilderFactory.get("batchTestStep")
                .<PaySum, PaySum>chunk(CHUNK_SIZE)
                .reader(batchTestReader(null))
                .writer(batchTestWriter())
                .build();
    }

    @Bean
    public JpaItemWriter<PaySum> batchTestWriter() {
        JpaItemWriter<PaySum> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(entityManagerFactory);
        return itemWriter;
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<PaySum> batchTestReader(@Value("#{jobParameters[tradeDate]}") String tradeDate) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("tradeDate", LocalDate.parse(tradeDate, FORMATTER));

        String className = PaySum.class.getName();
        String query = String.format(
                "SELECT new %s(p.txDate, SUM(p.amount)) " +
                "FROM Pay p " +
                "WHERE p.txDate =:tradeDate " +
                "GROUP BY p.txDate ", className
        );

        return new JpaPagingItemReaderBuilder<PaySum>()
                .pageSize(CHUNK_SIZE)
                .name("batchTestReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(query)
                .parameterValues(parameterMap)
                .build();
    }
}
