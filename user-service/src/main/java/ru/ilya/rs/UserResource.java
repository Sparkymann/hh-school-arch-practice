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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import ru.ilya.api.UserDto;
import ru.ilya.service.UserService;

@Path("/user")
@RestController
public class UserResource {
  private final static Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
  private final UserService userService;
  @Value("${spring.cloud.consul.discovery.instance-id}")
  private String instanceId;

  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(UserDto userDto) {
    if (userDto.id() != null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Не надо передавать id при создании").build();
    }
    Long id = userService.createUser(userDto);
    return Response.status(Response.Status.CREATED).contentLocation(UriBuilder.fromMethod(UserResource.class, "getUser").build(id)).build();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") Long id) {
    UserDto userDto = userService.getUserById(id);
    if (userDto == null) {
      return Response.status(Response.Status.NOT_FOUND).entity("Нет такого пользователя").build();
    }
    return Response.status(Response.Status.OK).entity(userDto).build();
  }

  @GET
  @Path("/exists/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response exists(@PathParam("name") String name) throws InterruptedException {
    // Второй инстанс будет "помедленнее" чем первый
    if ("Slow".equals(name) && instanceId.contains("2")) {
      LOGGER.warn("slow user, засыпаем");
      Thread.sleep(500);
    }
    if ("Error".equals(name)) {
      LOGGER.warn("error user");
      return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR)
          .header("X-Instance-Id", instanceId)
          .build();
    }
    return Response
        .status(Response.Status.OK)
        .header("X-Instance-Id", instanceId)
        .entity(userService.exists(name))
        .build();
  }
}
