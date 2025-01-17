package ru.ilya.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Package {
  @Id
  @GeneratedValue
  private Long id;
  @Column
  private String sender;
  @Column
  private String receiver;
  @Column
  private PackageStatus status;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public PackageStatus getStatus() {
    return status;
  }

  public void setStatus(PackageStatus status) {
    this.status = status;
  }
}
