package ru.ilya.rs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.springframework.web.bind.annotation.RestController;
import ru.ilya.api.PackageDto;
import ru.ilya.service.PackageService;

@Path("/package")
@RestController
public class PackageResource {
  private final PackageService packageService;

  public PackageResource(PackageService packageService) {
    this.packageService = packageService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response send(PackageDto packageDto) {
    if (packageDto.id() != null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Не надо передавать id при создании").build();
    }
    Long id = packageService.send(packageDto);
    return Response.status(Response.Status.CREATED).contentLocation(UriBuilder.fromMethod(PackageResource.class, "getPackage").build(id)).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPackage(@PathParam("id") Long id) {
    PackageDto packageDto = packageService.getPackageById(id);
    if (packageDto == null) {
      return Response.status(Response.Status.NOT_FOUND).entity("Нет такого отправления").build();
    }
    return Response.status(Response.Status.OK).entity(packageDto).build();
  }
}
