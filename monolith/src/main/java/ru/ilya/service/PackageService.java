package ru.ilya.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.ilya.api.PackageDto;
import ru.ilya.dao.PackageDao;
import ru.ilya.model.Package;

@Service
public class PackageService {
  private final PackageDao packageDao;
  private final UserService userService;

  public PackageService(PackageDao packageDao, UserService userService) {
    this.packageDao = packageDao;
    this.userService = userService;
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
    return Arrays.stream(names).filter(name -> !userService.exists(name)).collect(Collectors.toList());
  }
}
