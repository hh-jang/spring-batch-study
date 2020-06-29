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

@Slf4j
@AllArgsConstructor
@Configuration
public class TransactionProcessorJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job transactionProcessorJob() {
        return jobBuilderFactory.get("transactionProcessorJob")
                .start(transactionProcessorStep())
                .build();
    }

    @Bean
    public Step transactionProcessorStep() {
        return stepBuilderFactory.get("transactionProcessorStep")
                .<Teacher, ClassInformation>chunk(MAXIMUM_CHUNK_SIZE)
                .reader(transactionProcessorReader())
                .processor(transactionProcessorProcessor())
                .writer(transactionProcessorWriter())
                .build();
    }

    private ItemWriter<ClassInformation> transactionProcessorWriter() {
        return items -> {
            items.stream().forEach(o -> {
                log.info("class information : {}", o);
            });
        };
    }

    private ItemProcessor<Teacher, ClassInformation> transactionProcessorProcessor() {
        return teacher -> {
            return new ClassInformation(teacher.getName(), teacher.getStudents().size());
        };
    }

    private JpaPagingItemReader<Teacher> transactionProcessorReader() {
        return new JpaPagingItemReaderBuilder<Teacher>()
                .entityManagerFactory(entityManagerFactory)
                .name("transactionProcessorReader")
                .pageSize(MAXIMUM_CHUNK_SIZE)
                .queryString("SELECT t FROM Teacher t")
                .build();
    }
}
