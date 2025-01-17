package ru.ilya.service;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ilya.api.PackageDto;
import ru.ilya.dao.PackageDao;
import ru.ilya.model.Package;
import ru.ilya.model.PackageStatus;

@Service
public class PackageService {
  private final static Logger LOGGER = LoggerFactory.getLogger(PackageService.class);
  private final PackageDao packageDao;
  private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;

  public PackageService(PackageDao packageDao, KafkaTemplate<String, Map<String, String>> kafkaTemplate) {
    this.packageDao = packageDao;
    this.kafkaTemplate = kafkaTemplate;
  }

  public Long accept(PackageDto packageDto) {
    Package pack = new Package();
    pack.setSender(packageDto.sender());
    pack.setReceiver(packageDto.receiver());
    pack.setStatus(PackageStatus.ACCEPTED);
    packageDao.save(pack);

    Map<String, String> message = new HashMap<>();
    message.put("sender", packageDto.sender());
    message.put("receiver", packageDto.receiver());
    message.put("id", pack.getId().toString());
    kafkaTemplate
        .send("accepted", message)
        .thenAccept(sendResult -> LOGGER.info("Result: {}", sendResult));
    return pack.getId();
  }

  public PackageDto getPackageById(Long id) {
    return packageDao.findById(id).map(pack -> new PackageDto(id, pack.getSender(), pack.getReceiver(), pack.getStatus().name())).orElse(null);
  }

  @KafkaListener(id = "package-service-validated", topics = "validated")
  public void send(Map<String, String> message) {
    LOGGER.info("Confirmed message: {}", message);
    Long id = Long.valueOf(message.get("id"));
    Package pack = packageDao.findById(id).orElseThrow();
    pack.setStatus(PackageStatus.SENT);
    packageDao.save(pack);

    Map<String, String> answer = new HashMap<>();
    answer.put("sender", pack.getSender());
    answer.put("receiver", pack.getReceiver());
    kafkaTemplate.send("sent", answer);
  }

  @KafkaListener(id = "package-service-declined", topics = "declined")
  public void decline(Map<String, String> message) {
    LOGGER.info("Declined message: {}", message);
    Long id = Long.valueOf(message.get("id"));
    Package pack = packageDao.findById(id).orElseThrow();
    pack.setStatus(PackageStatus.DECLINED);
    packageDao.save(pack);
  }
}
