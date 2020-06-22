package com.hhjang.springbatch.job.itemreader;

import com.hhjang.springbatch.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

/**
 * CursorItemReader를 이용한 example
 * Cursor는 DB와의 '하나'의 Connection을 이용하여 Batch가 종료될때까지 사용한다.
 * 그러므로 사용하려면 SocketTimeOut 설정을 길게 잡아야함.
 * 그리고 Thread Safe 하지 않으므로 걍 왠만하면 PageItemReader 쓰는걸로...
 * https://jojoldu.tistory.com/336?category=902551
 * https://stackoverflow.com/questions/31568669/pagingitemreader-vs-cursoritemreader-in-spring-batch
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int MAXIMUM_CHUNK_SIZE = 10;

    @Bean
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get("jdbcCursorItemReaderJob")
                .start(jdbcCursorItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcCursorItemReaderStep() {
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")
                .<Pay, Pay>chunk(MAXIMUM_CHUNK_SIZE)
                .reader(jdbcCursorItemReader())
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<Pay> jdbcCursorItemWriter() {
        return items -> {
            for (Pay o : items) {
                log.info("Current Pay={}", o);
            }
        };
    }

    private JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .fetchSize(MAXIMUM_CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT id, amount, tx_date_time, tx_name FROM pay")
                .name("jdbcCursorItemReader")
                .build();
    }
}
