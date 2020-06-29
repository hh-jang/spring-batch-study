package com.hhjang.springbatch.job.itemreader;

import com.hhjang.springbatch.pay.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static com.hhjang.springbatch.job.itemreader.JpaPagingItemReaderJobConfiguration.*;

/**
 * JPA를 지원하기 위한 JpaPagingItemReader example
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class JpaPagingItemReaderJobConfiguration {
    public static final String JOB_NAME = "jpaPagingItemReaderJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    // temp
    @Bean
    @JobScope
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean
    @StepScope
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Pay, Pay>chunk(MAXIMUM_CHUNK_SIZE)
                .writer(jpaPagingItemWriter())
                .reader(jpaPagingItemReader())
                .processor(jpaPagingItemProcessor())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Pay> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(MAXIMUM_CHUNK_SIZE)
                .queryString("SELECT p FROM Pay p WHERE p.successStatus = false")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Pay, Pay> jpaPagingItemProcessor() {
        return item -> {
            item.success();
            return item;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<? super Object> jpaPagingItemWriter() {
        return list -> {
            list.stream().forEach(o -> {
                log.info("Current Pay={}", o);
            });
        };
    }
}
