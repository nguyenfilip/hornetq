package net.nguyen.journal.inspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan(basePackageClasses=Config.class)
public class Config {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
