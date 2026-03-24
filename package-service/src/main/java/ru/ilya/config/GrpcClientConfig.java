package ru.ilya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import ru.ilya.grpc.gen.UserServiceGrpc;

@Configuration
public class GrpcClientConfig {

  @Bean
  public UserServiceGrpc.UserServiceBlockingStub userServiceStub(GrpcChannelFactory channelFactory) {
    return UserServiceGrpc.newBlockingStub(channelFactory.createChannel("user-service"));
  }
}