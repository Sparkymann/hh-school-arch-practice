package ru.ilya.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ilya.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
  boolean existsByName(String name);
}
