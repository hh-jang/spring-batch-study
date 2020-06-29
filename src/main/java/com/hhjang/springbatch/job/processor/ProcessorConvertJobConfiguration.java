package com.hhjang.springbatch.job.processor;

import com.hhjang.springbatch.teacher.Teacher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@AllArgsConstructor
@Configuration
public class ProcessorConvertJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job processorConvertJob() {
        return jobBuilderFactory.get("processorConvertJob")
                .start(processorConvertStep())
                .build();
    }

    @Bean
    public Step processorConvertStep() {
        return stepBuilderFactory.get("processorConvertStep")
                .<Teacher, String>chunk(MAXIMUM_CHUNK_SIZE)
                .reader(processorConvertReader())
                .processor(processorConvertProcessor())
                .writer(processorConvertWeiter())
                .build();
    }

    private ItemWriter<String> processorConvertWeiter() {
        return items -> {
            items.stream().forEach(o -> {
                log.info("teacher name : {}", o);
            });
        };
    }

    private ItemProcessor<Teacher, String> processorConvertProcessor() {
        return item -> {
            return item.getName();
        };
    }

    private JpaPagingItemReader<Teacher> processorConvertReader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .entityManagerFactory(entityManagerFactory)
                .name("processorConvertReader")
                .pageSize(MAXIMUM_CHUNK_SIZE)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }
}
