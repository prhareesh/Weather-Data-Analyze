package com.timesync.securin.batch;

import com.timesync.securin.entity.WeatherData;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<WeatherData> reader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        return new FlatFileItemReaderBuilder<WeatherData>()
                .name("weatherReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("date", "temperature", "humidity")
                .targetType(WeatherData.class)
                .build();
    }


    @Bean
    public ItemProcessor<WeatherData, WeatherData> processor() {
        return item -> {
            // Example logic: Convert temperature to Celsius if needed
            if (item.getTemperature() != null) {
                item.setTemperature((item.getTemperature() - 32) * 5 / 9);
            }
            return item;
        };
    }


    @Bean
    public JpaItemWriter<WeatherData> writer(EntityManagerFactory emf) {
        return new JpaItemWriterBuilder<WeatherData>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public Step weatherStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            FlatFileItemReader<WeatherData> reader,
                            JpaItemWriter<WeatherData> writer) {

        return new StepBuilder("weatherStep", jobRepository)
                .<WeatherData, WeatherData>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public Job weatherJob(JobRepository jobRepository,
                          Step weatherStep) {

        return new JobBuilder("weatherJob", jobRepository)
                .start(weatherStep)
                .build();
    }
}