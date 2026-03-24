package ru.ilya.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import ru.ilya.api.UserDto;
import ru.ilya.grpc.gen.CreateUserRequest;
import ru.ilya.grpc.gen.CreateUserResponse;
import ru.ilya.grpc.gen.ExistsUserRequest;
import ru.ilya.grpc.gen.ExistsUserResponse;
import ru.ilya.grpc.gen.GetUserRequest;
import ru.ilya.grpc.gen.GetUserResponse;
import ru.ilya.grpc.gen.UserServiceGrpc;
import ru.ilya.service.UserService;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

  private final UserService userService;

  public UserGrpcService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
    if (request.getName().isBlank()) {
      responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Имя не должно быть пустым").asRuntimeException());
      return;
    }
    Long id = userService.createUser(new UserDto(null, request.getName()));
    responseObserver.onNext(CreateUserResponse.newBuilder().setId(id).build());
    responseObserver.onCompleted();
  }

  @Override
  public void getUser(GetUserRequest request, StreamObserver<GetUserResponse> responseObserver) {
    UserDto userDto = userService.getUserById(request.getId());
    if (userDto == null) {
      responseObserver.onNext(GetUserResponse.newBuilder().setFound(false).build());
    } else {
      responseObserver.onNext(GetUserResponse.newBuilder()
          .setFound(true)
          .setId(userDto.id())
          .setName(userDto.name())
          .build());
    }
    responseObserver.onCompleted();
  }

  @Override
  public void existsUser(ExistsUserRequest request, StreamObserver<ExistsUserResponse> responseObserver) {
    boolean exists = userService.exists(request.getName());
    responseObserver.onNext(ExistsUserResponse.newBuilder().setExists(exists).build());
    responseObserver.onCompleted();
  }
}