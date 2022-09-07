package com.capstone;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.capstone.service.AudioConversionService;

@SpringBootApplication
public class AudioComparisonBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudioComparisonBackendApplication.class, args);
	}

	@Bean(name = "AudioConversionService")
	@ConditionalOnProperty(prefix = "audioconversion", name = "service", havingValue = "default")
	AudioConversionService  audioconversionDefault() {
		return new AudioConversionService();
	}
}
