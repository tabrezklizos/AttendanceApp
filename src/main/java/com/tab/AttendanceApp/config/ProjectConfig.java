package com.tab.AttendanceApp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper mapper(){
      return new ModelMapper();
  }

    @Bean
    public AuditConfig awareRef(){return new AuditConfig();}
}
