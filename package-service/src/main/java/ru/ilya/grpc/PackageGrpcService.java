package ru.ilya.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import ru.ilya.api.PackageDto;
import ru.ilya.grpc.gen.GetPackageRequest;
import ru.ilya.grpc.gen.GetPackageResponse;
import ru.ilya.grpc.gen.PackageServiceGrpc;
import ru.ilya.grpc.gen.SendPackageRequest;
import ru.ilya.grpc.gen.SendPackageResponse;
import ru.ilya.service.PackageService;

@GrpcService
public class PackageGrpcService extends PackageServiceGrpc.PackageServiceImplBase {

  private final PackageService packageService;

  public PackageGrpcService(PackageService packageService) {
    this.packageService = packageService;
  }

  @Override
  public void sendPackage(SendPackageRequest request, StreamObserver<SendPackageResponse> responseObserver) {
    try {
      Long id = packageService.send(new PackageDto(null, request.getSender(), request.getReceiver()));
      responseObserver.onNext(SendPackageResponse.newBuilder().setId(id).build());
      responseObserver.onCompleted();
    } catch (IllegalArgumentException e) {
      responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  @Override
  public void getPackage(GetPackageRequest request, StreamObserver<GetPackageResponse> responseObserver) {
    PackageDto packageDto = packageService.getPackageById(request.getId());
    if (packageDto == null) {
      responseObserver.onNext(GetPackageResponse.newBuilder().setFound(false).build());
    } else {
      responseObserver.onNext(GetPackageResponse.newBuilder()
          .setFound(true)
          .setId(packageDto.id())
          .setSender(packageDto.sender())
          .setReceiver(packageDto.receiver())
          .build());
    }
    responseObserver.onCompleted();
  }
}