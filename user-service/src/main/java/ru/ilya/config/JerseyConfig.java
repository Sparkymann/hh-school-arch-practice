package ru.ilya.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ilya.rs.UserResource;

@Configuration
public class JerseyConfig {

  @Bean
  public ResourceConfig resourceConfig() {
    ResourceConfig resourceConfig = new ResourceConfig();
    resourceConfig.register(UserResource.class);
    return resourceConfig;
  }
}
