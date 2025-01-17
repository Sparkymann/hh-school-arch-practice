package ru.ilya.config;

import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

  @Bean
  @LoadBalanced
  public RestClient.Builder clientBuilder(RestClientBuilderConfigurer restClientBuilderConfigurer) {
    return restClientBuilderConfigurer.configure(RestClient.builder());
  }

}
