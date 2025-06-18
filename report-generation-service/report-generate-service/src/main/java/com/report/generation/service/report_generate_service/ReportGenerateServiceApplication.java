package com.report.generation.service.report_generate_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReportGenerateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportGenerateServiceApplication.class, args);
	}

}
