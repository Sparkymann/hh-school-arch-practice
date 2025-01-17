package ru.ilya.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ilya.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
  boolean existsByName(String name);
  @Modifying
  @Query("update User u set u.sentPackagesCount = u.sentPackagesCount + 1 where u.name = :name")
  void increaseSentCounter(@Param("name") String name);
  @Modifying
  @Query("update User u set u.receivedPackagesCount = u.receivedPackagesCount + 1 where u.name = :name")
  void increaseReceivedCounter(@Param("name") String name);
}
