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
public class ProcessorFilterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job processorFilterJob() {
        return jobBuilderFactory.get("processorFilterJob")
                .start(processorFilterStep())
                .build();
    }

    @Bean
    public Step processorFilterStep() {
        return stepBuilderFactory.get("processorFilterStep")
                .<Teacher, String>chunk(MAXIMUM_CHUNK_SIZE)
                .reader(processorFilterReader())
                .processor(processorFilterProcessor())
                .writer(processorFilterWeiter())
                .build();
    }

    private ItemWriter<String> processorFilterWeiter() {
        return items -> {
            items.stream().forEach(o -> {
                log.info("teacher id : {}", o);
            });
        };
    }

    private ItemProcessor<Teacher, String> processorFilterProcessor() {
        return teach -> {
            boolean isEven = false;

            if(teach.getId() % 2 == 0) isEven = true;

            if(isEven) return teach.getName();

            log.info("id is Odd target ignore, id : {}", teach.getId());
            return null;
        };
    }

    private JpaPagingItemReader<Teacher> processorFilterReader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .entityManagerFactory(entityManagerFactory)
                .name("processorFilterReader")
                .pageSize(MAXIMUM_CHUNK_SIZE)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }
}
