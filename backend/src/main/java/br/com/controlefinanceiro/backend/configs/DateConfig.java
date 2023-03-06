package br.com.controlefinanceiro.backend.configs;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class DateConfig {

	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    public static LocalDateSerializer LOCAL_DATE_SERIALIZER = new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT));

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATETIME_SERIALIZER);
        module.addSerializer(LOCAL_DATE_SERIALIZER);
        return new ObjectMapper()
                .registerModule(module);
    }
    
}
