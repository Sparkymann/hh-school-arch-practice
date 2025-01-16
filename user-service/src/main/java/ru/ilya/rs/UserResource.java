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
import ru.ilya.api.UserDto;
import ru.ilya.service.UserService;

@Path("/user")
@RestController
public class UserResource {
  private final UserService userService;

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
  public Response exists(@PathParam("name") String name) {
    return Response.status(Response.Status.OK).entity(userService.exists(name)).build();
  }
}
