package ru.ilya.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.ilya.api.PackageDto;
import ru.ilya.dao.PackageDao;
import ru.ilya.model.Package;

@Service
public class PackageService {
  private final PackageDao packageDao;
  private final RestClient userServiceClient;

  public PackageService(PackageDao packageDao) {
    this.packageDao = packageDao;
    this.userServiceClient = RestClient.builder().baseUrl("http://172.17.0.1:8080/user").build();
  }

  public Long send(PackageDto packageDto) {
    List<String> undefinedUsers = getUndefinedUsers(packageDto.sender(), packageDto.receiver());
    if (!undefinedUsers.isEmpty()) {
      throw new IllegalArgumentException("Неизвестные пользователи: %s".formatted(undefinedUsers));
    }
    Package pack = new Package();
    pack.setSender(packageDto.sender());
    pack.setReceiver(packageDto.receiver());
    packageDao.save(pack);
    return pack.getId();
  }

  public PackageDto getPackageById(Long id) {
    return packageDao.findById(id).map(pack -> new PackageDto(id, pack.getSender(), pack.getReceiver())).orElse(null);
  }

  private List<String> getUndefinedUsers(String... names) {
    ArrayList<String> undefinedUsers = new ArrayList<>();
    for (String name : names) {
      Boolean exists = userServiceClient.get().uri("/exists/{name}", name).retrieve().body(Boolean.class);
      if (Boolean.FALSE.equals(exists)) {
        undefinedUsers.add(name);
      }
    }
    return undefinedUsers;
  }
}
