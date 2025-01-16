package ru.ilya.service;

import org.springframework.stereotype.Service;
import ru.ilya.api.UserDto;
import ru.ilya.dao.UserDao;
import ru.ilya.model.User;

@Service
public class UserService {
  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public Long createUser(UserDto userDto) {
    User user = new User();
    user.setName(userDto.name());
    userDao.save(user);
    return user.getId();
  }

  public UserDto getUserById(Long id) {
    return userDao.findById(id).map(user -> new UserDto(id, user.getName())).orElse(null);
  }

  public boolean exists(String name) {
    return userDao.existsByName(name);
  }
}
