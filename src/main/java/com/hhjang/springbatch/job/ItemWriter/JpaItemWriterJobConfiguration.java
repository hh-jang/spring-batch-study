package com.hhjang.springbatch.job.ItemWriter;

import com.hhjang.springbatch.entity.pay.Pay;
import com.hhjang.springbatch.entity.pay.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job jpaItemWriterJob() {
        return jobBuilderFactory.get("JpaItemWriterJob")
                .start(jpaItemWriterStep())
                .build();
    }

    @Bean
    public Step jpaItemWriterStep() {
        return stepBuilderFactory.get("JpaItemWriterStep")
                .<Pay, Pay2>chunk(MAXIMUM_CHUNK_SIZE)
                .writer(jpaItemWriter())
                .processor(jpaItemWriterProcessor())
                .reader(jpaItemWriterReader())
                .build();
    }

    private ItemProcessor<Pay, Pay2> jpaItemWriterProcessor() {
        return pay -> new Pay2(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
    }

    private JpaPagingItemReader<Pay> jpaItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .pageSize(MAXIMUM_CHUNK_SIZE)
                .queryString("SELECT p FROM Pay p")
                .name("jpaItemWriterReader")
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    private JpaItemWriter<Pay2> jpaItemWriter() {
        return new JpaItemWriterBuilder<Pay2>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
