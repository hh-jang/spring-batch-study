package com.hhjang.springbatch.job.processor;

import com.hhjang.springbatch.entity.classinformation.ClassInformation;
import com.hhjang.springbatch.entity.teacher.Teacher;
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

// writer에서 student 조회 확인, lazy loading을 확인할 수 있음

@Slf4j
@AllArgsConstructor
@Configuration
public class TransactionWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job transactionWriterJob() {
        return jobBuilderFactory.get("transactionWriterJob")
                .start(transactionWriterStep())
                .build();
    }

    @Bean
    public Step transactionWriterStep() {
        return stepBuilderFactory.get("transactionWriterStep")
                .<Teacher, Teacher>chunk(MAXIMUM_CHUNK_SIZE)
                .reader(transactionWriterReader())
                .writer(transactionWriterWriter())
                .build();
    }

    private ItemWriter<Teacher> transactionWriterWriter() {
        return teachers -> {
            teachers.stream().forEach(teacher -> {
                log.info("teacher={}, studentSize={}", teacher, teacher.getStudents().size());
            });
        };
    }

    private JpaPagingItemReader<Teacher> transactionWriterReader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .entityManagerFactory(entityManagerFactory)
                .name("transactionWriterReader")
                .pageSize(MAXIMUM_CHUNK_SIZE)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }
}
