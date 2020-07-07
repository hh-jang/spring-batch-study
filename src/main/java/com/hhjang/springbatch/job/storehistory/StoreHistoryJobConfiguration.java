package com.hhjang.springbatch.job.storehistory;

import com.hhjang.springbatch.common.JpaPagingFetchItemReader;
import com.hhjang.springbatch.entity.store.Store;
import com.hhjang.springbatch.entity.storehistory.StoreHistory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * JPA N+1 example
 */

@Slf4j
@Configuration
@AllArgsConstructor
public class StoreHistoryJobConfiguration {
    public static final String JOB_NAME = "storeHistory";
    public static final String STEP_NAME = JOB_NAME + "Step";

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private EntityManagerFactory entityManager;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job storeHistoryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(storeHistoryStep())
                .build();
    }
    @Bean
    public Step storeHistoryStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Store, StoreHistory>chunk(MAXIMUM_CHUNK_SIZE)
                .reader(storeHistoryReader())
                .processor(storeHistoryProcessor())
                .writer(storeHistoryWriter())
                .build();
    }

    @Bean
    public JpaItemWriter<StoreHistory> storeHistoryWriter() {
        return new JpaItemWriterBuilder<StoreHistory>()
                .entityManagerFactory(entityManager)
                .build();
    }

    @Bean
    public ItemProcessor<Store, StoreHistory> storeHistoryProcessor() {
        return item -> new StoreHistory(item, item.getProducts(), item.getEmployees());
    }

    @Bean
    @StepScope
    public JpaPagingFetchItemReader<Store> storeHistoryReader() {
        JpaPagingFetchItemReader<Store> itemReader = new JpaPagingFetchItemReader<>();
        itemReader.setEntityManagerFactory(entityManager);
        itemReader.setQueryString("SELECT s FROM Store s");
        itemReader.setPageSize(MAXIMUM_CHUNK_SIZE);
        itemReader.setTransacted(false);

        return itemReader;
    }
}
