package com.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.capstone.service.WaveFormService;



@SpringBootApplication
public class CapstoneBackendApplication{

	public static void main(String[] args) {
		SpringApplication.run(CapstoneBackendApplication.class, args);
	}

	@Bean(name = "WaveFormService")
	@ConditionalOnProperty(prefix="waveform", name="service", havingValue = "default")
	public WaveFormService gradebookDefault() {
		return new WaveFormService();
	}
}
