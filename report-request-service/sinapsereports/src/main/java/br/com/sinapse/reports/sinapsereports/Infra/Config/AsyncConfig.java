package br.com.sinapse.reports.sinapsereports.Infra.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import br.com.sinapse.reports.sinapsereports.Application.Dtos.ReportRequestResponseDto;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "reportTaskExecutor")
    public Executor reportTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);

        executor.setMaxPoolSize(10);

        executor.setQueueCapacity(25);

        executor.setThreadNamePrefix("ReportGenerator-");

        executor.initialize();

        return executor;
    }

    @Bean
    public KafkaTemplate<String, ReportRequestResponseDto> kafkaTemplate(
            ProducerFactory<String, ReportRequestResponseDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}