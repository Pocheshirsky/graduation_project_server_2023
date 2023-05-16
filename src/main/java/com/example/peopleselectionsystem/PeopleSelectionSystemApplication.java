package com.example.peopleselectionsystem;

import com.example.storage.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "com.example" })
@ConfigurationPropertiesScan("com.example")
@ComponentScan({ "com.example" })
@EnableJpaRepositories(basePackages = { "com.example" })
@Configuration
public class PeopleSelectionSystemApplication {

    @Autowired
    StorageService storageService;


    public static void main(String[] args) {
        SpringApplication.run(PeopleSelectionSystemApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        storageService.init();
        System.err.println("Yaaah, I am running........");
    }

}
