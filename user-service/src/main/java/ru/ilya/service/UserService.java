package ru.ilya.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilya.api.UserDto;
import ru.ilya.dao.UserDao;
import ru.ilya.model.User;

@Service
public class UserService {
  private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
  private final UserDao userDao;
  private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;

  public UserService(UserDao userDao, KafkaTemplate<String, Map<String, String>> kafkaTemplate) {
    this.userDao = userDao;
    this.kafkaTemplate = kafkaTemplate;
  }

  public Long createUser(UserDto userDto) {
    User user = new User();
    user.setName(userDto.name());
    userDao.save(user);
    return user.getId();
  }

  public UserDto getUserById(Long id) {
    return userDao.findById(id).map(user -> new UserDto(id, user.getName(), user.getSentPackagesCount(), user.getReceivedPackagesCount())).orElse(null);
  }

  public boolean exists(String name) {
    return userDao.existsByName(name);
  }

  @KafkaListener(id = "user-service-validate-listener", topics = "accepted")
  public void validate(@Payload Map<String, String> message) {
    LOGGER.info("Message for validation received: {}", message);
    String sender = message.get("sender");
    String receiver = message.get("receiver");
    List<String> undefinedUsers = undefinedUsers(sender, receiver);
    if (undefinedUsers.isEmpty()) {
      Map<String, String> answer = new HashMap<>();
      answer.put("id", message.get("id"));
      kafkaTemplate.send("validated", answer);
    } else {
      Map<String, String> answer = new HashMap<>();
      answer.put("id", message.get("id"));
      answer.put("reason", "Неизвестные пользователи: %s".formatted(undefinedUsers));
      kafkaTemplate.send("declined", answer);
    }
  }

  @KafkaListener(id = "user-service-sent-listener", topics = "sent")
  @Transactional
  public void increaseCounters(Map<String, String> message) {
    userDao.increaseSentCounter(message.get("sender"));
    userDao.increaseReceivedCounter(message.get("receiver"));
  }

  private List<String> undefinedUsers(String... names) {
    return Arrays.stream(names).filter(Predicate.not(this::exists)).toList();
  }
}
