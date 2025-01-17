package ru.ilya.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String name;
  @Column
  private int sentPackagesCount = 0;
  @Column
  private int receivedPackagesCount = 0;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSentPackagesCount() {
    return sentPackagesCount;
  }

  public void setSentPackagesCount(Integer sentPackagesCount) {
    this.sentPackagesCount = sentPackagesCount;
  }

  public Integer getReceivedPackagesCount() {
    return receivedPackagesCount;
  }

  public void setReceivedPackagesCount(Integer receivedPackagesCount) {
    this.receivedPackagesCount = receivedPackagesCount;
  }
}
