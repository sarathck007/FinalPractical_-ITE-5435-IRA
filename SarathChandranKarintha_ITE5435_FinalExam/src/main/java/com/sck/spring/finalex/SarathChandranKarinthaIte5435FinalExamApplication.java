package com.sck.spring.finalex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
@SpringBootApplication
public class SarathChandranKarinthaIte5435FinalExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(SarathChandranKarinthaIte5435FinalExamApplication.class, args);
	}

	/**
     * Configure ObjectMapper for Jackson JSON processing
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
